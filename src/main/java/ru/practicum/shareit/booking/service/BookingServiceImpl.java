package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import ru.practicum.shareit.exeption.NoAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public BookingDto create(int userId, CreateBookingDto newBookingDto) {
        Booking booking = bookingMapper.toBooking(userId, newBookingDto);
        if (booking.getItem().getAvailable().equals(false)) {
            throw new ValidationException("Item недоступен для бронирования");
        }
        if (Objects.equals(booking.getBooker().getId(), booking.getItem().getOwner().getId())) {
            throw new NotFoundException("item уже ваш");
        }
        if (booking.getStart() != null && booking.getEnd() != null && !booking.getEnd().isAfter(booking.getStart())) {
            log.warn("Дата окончания бронирования должна быть после даты начала");
            throw new ValidationException("Дата окончания бронирования должна быть после даты начала");
        }

        Booking bookingCreated = bookingRepository.save(booking);

        log.info("создано бронирование: {}", bookingCreated);
        return bookingMapper.toBookingDto(bookingCreated);
    }

    @Transactional
    @Override
    public BookingDto confirmStatus(int ownerId, int bookingId, boolean approved) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", ownerId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
        Item item = booking.getItem();
        int ownerIds = item.getOwner().getId();
        if (!(ownerIds == ownerId)) {
            log.warn("Пользователь с id {} не владеет вещью с id {}", ownerId, item.getId());
            throw new NotFoundException(
                    String.format("Пользователь с id %d не владеет вещью с id %d", ownerId, item.getId()));
        }
        BookingStatus status;
        if (approved) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                log.warn("Бронирование с id {} уже подтверждено", bookingId);
                throw new ValidationException(String.format("Бронирование с id %d уже подтверждено", bookingId));
            }
            status = BookingStatus.APPROVED;
        } else {
            if (booking.getStatus().equals(BookingStatus.REJECTED)) {
                log.warn("Бронирование с id {} уже отклонено", bookingId);
                throw new ValidationException(String.format("Бронирование с id %d уже отклонено", bookingId));
            }
            status = BookingStatus.REJECTED;
        }
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(int userId, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("бронирование с id " + bookingId + " не найдено"));

        int bookingOwnerId = booking.getBooker().getId();
        int bookingItemOwnerId = booking.getItem().getOwner().getId();

        if (userId == bookingOwnerId || userId == bookingItemOwnerId) {
            return bookingMapper.toBookingDto(booking);
        }

        throw new NoAccessException("пользователь с id: " + userId + " не может забронировать");
    }


    @Override
    public List<BookingDto> getAllByUser(int userId, String state, int from, int size) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(from<0 || size<0){
            throw new ValidationException("неверные параметры");
        }

        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        List<Booking> bookings = filterBookings(state,
                bookingRepository.findAllByBooker(booker, pageRequest)
                );

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> filterBookings(String state, List<Booking> bookings) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case "CURRENT":
                return bookings.stream()
                        .filter(booking -> now.isAfter(booking.getStart()) && now.isBefore(booking.getEnd()))
                        .collect(Collectors.toList());
            case "PAST":
                return bookings.stream()
                        .filter(booking -> now.isAfter(booking.getEnd()))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookings.stream()
                        .filter(booking -> now.isBefore(booking.getStart()) || now.isBefore(booking.getEnd()))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .collect(Collectors.toList());
            case "ALL":
                return bookings;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getAllByItemOwner(int userId, String state, int from, int size) {
        User itemOwner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(from<0 || size<0){
            throw new ValidationException("неверные параметры");
        }

        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        List<Booking> bookings = filterBookings(
               state, bookingRepository.findAllByItemOwner(itemOwner, pageRequest));

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}