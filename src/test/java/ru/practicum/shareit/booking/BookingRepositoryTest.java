package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository repository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    private Booking booking;
    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setName("name");
        owner.setEmail("e@mail.ru");
        owner = userRepo.save(owner);

        booker = new User();
        booker.setName("name1");
        booker.setEmail("e1@mail.ru");
        booker = userRepo.save(booker);

        item = new Item();
        item.setName("item");
        item.setDescription("itemdesc");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepo.save(item);

        LocalDateTime now = LocalDateTime.now();
        booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(now.plusDays(2));
        booking.setEnd(now.plusDays(4));
        booking.setStatus(BookingStatus.APPROVED);
        booking = repository.save(booking);
    }

    @Test
    public void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void findBookingsAtSameTime() {
        LocalDateTime start = booking.getEnd().plusSeconds(5);
        LocalDateTime end = booking.getEnd().plusSeconds(25);
        BookingStatus status = BookingStatus.APPROVED;
        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("select b from Booking b where (b.item.id = :itemId) and " +
                        "(b.status = :status) and " +
                        "(b.start between :start and :end " +
                        "OR b.end between :start and :end " +
                        "OR b.start <= :start AND b.end >= :end)", Booking.class);
        List<Booking> bookings = query
                .setParameter("itemId", item.getId())
                .setParameter("status", status)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
        start = booking.getStart().plusSeconds(5);
        bookings = query
                .setParameter("itemId", item.getId())
                .setParameter("status", status)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }
}