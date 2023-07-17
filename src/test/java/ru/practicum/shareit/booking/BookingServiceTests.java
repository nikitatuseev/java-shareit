package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTests {
    private final EntityManager em;

    @Test
    void findByState() {
        User owner = makeUser("name1", "e1@mail.ru");
        User booker = makeUser("name2", "e2@mail.ru");
        em.persist(owner);
        em.persist(booker);

        Item item = makeAvailableItem(owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> sourceBookings = List.of(
                makeBookingInDto(now, now.plusDays(2), item.getId()),
                makeBookingInDto(now.plusDays(4), now.plusDays(6), item.getId()),
                makeBookingInDto(now.plusDays(8), now.plusDays(10), item.getId())
        );


        em.flush();

    }

    private BookingDto makeBookingInDto(LocalDateTime start,
                                        LocalDateTime end,
                                        int itemId) {
        return BookingDto.builder()
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .build();
    }

    private User makeUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private Item makeAvailableItem(User owner) {
        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(owner);
        return item;
    }
}