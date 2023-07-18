package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createBooking() {
        int userId = 1;
        CreateBookingDto newBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();
        Booking booking = new Booking();
        Item item = new Item();
        item.setAvailable(false);
        User user = new User();
        user.setId(userId);
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(newBookingDto.getItemId())).thenReturn(Optional.of(item));
    }

    @Test
    public void confirmStatus() {
        int ownerId = 1;
        int bookingId = 2;
        boolean approved = true;
        User owner = new User();
        owner.setId(ownerId);
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.WAITING);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto updatedBookingDto = bookingService.confirmStatus(ownerId, bookingId, approved);

        verify(bookingRepository, times(1)).save(booking);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    public void confirmStatusBookingExists() {
        int ownerId = 1;
        int bookingId = 2;
        boolean approved = true;
        User owner = new User();
        owner.setId(ownerId);
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.WAITING);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.confirmStatus(ownerId, bookingId, approved));
    }

    @Test
    public void getAllByUser() {
        int userId = 1;
        String state = "CURRENT";
        int from = 0;
        int size = 10;
        User user = new User();
        user.setId(userId);
        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().minusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(1));
        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.now().minusDays(2));
        booking2.setEnd(LocalDateTime.now().minusDays(1));
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBooker(user, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "start")))).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking1)).thenReturn(new BookingDto());
        when(bookingMapper.toBookingDto(booking2)).thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, from, size);

        verify(bookingRepository, times(1)).findAllByBooker(user, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "start")));
    }
}


