package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForView;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookingMapperTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toBookingDto() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        when(itemMapper.toItemDto(any(Item.class))).thenReturn(new ItemDto());

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
    }

    @Test
    public void toBooking() {
        int userId = 1;
        CreateBookingDto newBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();
        newBookingDto.setStart(LocalDateTime.now().plusDays(1));
        newBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        newBookingDto.setItemId(1);

        User booker = new User();
        Item item = new Item();
        item.setId(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(newBookingDto.getItemId())).thenReturn(Optional.of(item));

        Booking booking = bookingMapper.toBooking(userId, newBookingDto);

        assertEquals(booker, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(newBookingDto.getStart(), booking.getStart());
        assertEquals(newBookingDto.getEnd(), booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    public void toBookingDtoForView() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(new User());

        BookingDtoForView bookingDtoForView = bookingMapper.toBookingDtoForView(booking);

        assertEquals(booking.getId(), bookingDtoForView.getId());
        assertEquals(booking.getBooker().getId(), bookingDtoForView.getBookerId());
    }
}

