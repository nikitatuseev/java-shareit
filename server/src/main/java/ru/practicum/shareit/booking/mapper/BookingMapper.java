package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.dto.BookingDtoForView;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(itemMapper.toItemDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        return bookingDto;
    }

    public Booking toBooking(int userId, CreateBookingDto newBookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemRepository.findById(Math.toIntExact(newBookingDto.getItemId()))
                .orElseThrow(() -> new NotFoundException("Item с id " + newBookingDto.getItemId() + " не найден"));

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(newBookingDto.getStart());
        booking.setEnd(newBookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public BookingDtoForView toBookingDtoForView(Booking booking) {
        BookingDtoForView bookingDtoForView = new BookingDtoForView();
        bookingDtoForView.setId(booking.getId());
        bookingDtoForView.setBookerId(booking.getBooker().getId());
        return bookingDtoForView;
    }
}