package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exeption.NoAccessException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookTest {
    @Captor
    ArgumentCaptor<Booking> bookingArgumentCaptor;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void confirmStatus_whenFlagTrue_thenSaveAndBookingDtoReturned() {
        int ownerId = 1;
        int bookingId = 1;
        User owner = new User(ownerId, "name", "e@mail.com");

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));
        when(bookingRepository.save(booking))
                .thenReturn(booking);
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        BookingDto result = bookingService.confirmStatus(ownerId, bookingId, true);

        assertNotNull(result);
        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(booking, savedBooking);
        assertEquals(item, savedBooking.getItem());
        assertEquals(owner, savedBooking.getItem().getOwner());
        assertEquals(BookingStatus.APPROVED, savedBooking.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(userRepository, times(1)).findById(ownerId);
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(bookingRepository, userRepository, bookingMapper);
    }

    @Test
    void confirmStatus() {
        int ownerId = 1;
        int bookingId = 1;
        User owner = new User(ownerId, "name", "e@mail.com");
        Item item = new Item();
        item.setOwner(owner);
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));
        when(bookingRepository.save(booking))
                .thenReturn(booking);
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        BookingDto result = bookingService.confirmStatus(ownerId, bookingId, false);

        assertNotNull(result);
        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(booking, savedBooking);
        assertEquals(item, savedBooking.getItem());
        assertEquals(owner, savedBooking.getItem().getOwner());
        assertEquals(BookingStatus.REJECTED, savedBooking.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(userRepository, times(1)).findById(ownerId);
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(bookingRepository, userRepository, bookingMapper);
    }

    @Test
    void getById() {
        int userId = 1;
        int bookingId = 1;
        Item item = new Item();
        item.setOwner(new User(userId, "name", "e@mail.com"));

        Booking booking = new Booking();
        booking.setBooker(new User(2, "name1", "s@mail.com"));
        booking.setItem(item);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        BookingDto resultBookingDto = bookingService.getById(userId, bookingId);

        assertNotNull(resultBookingDto);
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void getByIdBooker() {
        int userId = 1;
        int bookingId = 1;
        Item item = new Item();
        item.setOwner(new User(2, "name", "e@mail.com"));
        Booking booking = new Booking();
        booking.setBooker(new User(userId, "name1", "s@mail.com"));
        booking.setItem(item);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        BookingDto resultBookingDto = bookingService.getById(userId, bookingId);

        assertNotNull(resultBookingDto);
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void getByIdNoAccess() {
        int userId = 1;
        int bookingId = 1;
        Item item = new Item();
        item.setOwner(new User(2, "name", "e@mail.com"));
        Booking booking = new Booking();
        booking.setBooker(new User(3, "name1", "s@mail.com"));
        booking.setItem(item);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        assertThrows(NoAccessException.class, () -> bookingService.getById(userId, bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getByIdBookingNotFound() {
        int userId = 1;
        int bookingId = 999;
        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getById(userId, bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllByUser() {
        int userId = 1;
        String state = "CURRENT";
        User booker = new User();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }

    @Test
    void getAllByUserStateWaiting() {
        int userId = 1;
        String state = "WAITING";

        User booker = new User();
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }


    @Test
    void getAllByUserStatePast() {
        int userId = 1;
        String state = "PAST";
        User booker = new User();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }

    @Test
    void getAllByUserUnknown() {
        int userId = 1;
        String state = "Unknown state";
        User booker = new User();
        Booking booking = new Booking();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.getAllByUser(userId, state, 0, 10));

        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verifyNoMoreInteractions(userRepository, bookingRepository);
    }

    @Test
    void getAllByUserStateFuture() {
        int userId = 1;
        String state = "FUTURE";
        User booker = new User();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }

    @Test
    void getAllByUserNotFound() {
        int userId = 999;
        String state = "CURRENT";
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getAllByUser(userId, state, 0, 10));

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getAllByItemOwner() {
        int userId = 1;
        String state = "ALL";
        User itemOwner = new User();
        Booking booking = new Booking();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(itemOwner));
        when(bookingRepository.findAllByItemOwner(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByItemOwner(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByItemOwner(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }

    @Test
    void getAllByUserRejected() {
        int userId = 1;
        String state = "REJECTED";
        User booker = new User();
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.REJECTED);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }

    @Test
    void getAllByUserAll() {
        int userId = 1;
        String state = "ALL";
        User booker = new User();
        Booking booking = new Booking();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking))
                .thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllByUser(userId, state, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bookingMapper).toBookingDto(bookingArgumentCaptor.capture());

        Booking resultBooking = bookingArgumentCaptor.getValue();

        assertSame(booking, resultBooking);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findAllByBooker(any(User.class), any(PageRequest.class));
        verify(bookingMapper, times(1)).toBookingDto(booking);
        verifyNoMoreInteractions(userRepository, bookingRepository, bookingMapper);
    }
}