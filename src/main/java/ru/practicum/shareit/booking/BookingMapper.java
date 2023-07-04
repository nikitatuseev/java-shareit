package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.dto.BookingDtoForView;
import ru.practicum.shareit.booking.dto.CreatBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;

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
        bookingDto.setStatus(booking.getStatus().toString());
        bookingDto.setStart(booking.getStart().toString());
        bookingDto.setEnd(booking.getEnd().toString());
        return bookingDto;
    }

    public Booking toBooking(int userId, CreatBookingDto creatBookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemRepository.findById(Math.toIntExact(creatBookingDto.getItemId()))
                .orElseThrow(() -> new NotFoundException("Item с id " + creatBookingDto.getItemId() + " не найден"));

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(creatBookingDto.getStart());
        booking.setEnd(creatBookingDto.getEnd());
        return booking;
    }

    public BookingDtoForView toBookingDtoForView(Booking booking) {
        BookingDtoForView bookingDtoForView = new BookingDtoForView();
        bookingDtoForView.setId(booking.getId());
        bookingDtoForView.setBookerId(booking.getBooker().getId());
        return bookingDtoForView;
    }
}


