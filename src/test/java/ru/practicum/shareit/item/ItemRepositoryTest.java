package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepo;

    private User owner;
    private Item item;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setName("name");
        owner.setEmail("name@mail.ru");
        owner = userRepo.save(owner);

        item = new Item();
        item.setName("item");
        item.setDescription("itemdesc");
        item.setAvailable(true);
        item.setOwner(owner);
        item = repository.save(item);
    }

    @Test
    void findByOwnerId() {
        List<Item> items = repository.findAllByOwner(owner, PageRequest.of(0, 1));
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
    }

    @Test
    void search() {
        String text = "item";
        List<Item> items = repository.searchWithPaging(text, PageRequest.of(0, 1)).getContent();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
    }
}

