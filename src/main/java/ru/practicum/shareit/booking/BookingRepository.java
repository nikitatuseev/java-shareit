package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker(User bookersOwner, Pageable pageable);

    //List<Booking> findAllByItem(Item item);

    Optional<Booking> findFirstByItemAndStartBeforeAndStatusNotOrderByEndDesc(Item item,
                                                                              LocalDateTime currentTime,
                                                                              BookingStatus status);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusNotOrderByStartAsc(Item item,
                                                                              LocalDateTime currentTime,
                                                                              BookingStatus status);

    Optional<Booking> findFirstByBookerAndItemAndStatusAndEndBefore(User user,
                                                                    Item item,
                                                                    BookingStatus status,
                                                                    LocalDateTime currentTime);

    List<Booking> findAllByItemOwner(User itemsOwner, Pageable pageable);
}