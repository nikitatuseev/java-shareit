package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    //List<Item> findAllByOwner(User owner);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCase(String nameSearch, String descriptionSearch);

    List<Item> findAllByOwner(User owner, PageRequest pageRequest);

    @Query(" select i from Item i where (lower(i.name) like concat('%', :text, '%') or lower(i.description) like concat('%', :text, '%')) " +
            " and i.available = true")
    Page<Item> searchWithPaging(@Param("text") String text, Pageable page);
}
