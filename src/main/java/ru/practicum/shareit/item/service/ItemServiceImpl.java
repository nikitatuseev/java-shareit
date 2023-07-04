package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoForView;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreatCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//у меня получается очень много дублирования и условий но я не могу додуматься как сделать легче
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemDto create(int ownerId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("имя не может быть пустым");
        }
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден")));
        Item itemCreated = itemRepository.save(item);

        log.info("добавлен item: {}", itemCreated);
        return itemMapper.toItemDto(itemCreated);
    }

    @Override
    public ItemDtoWithComments getById(int userId, int id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + id + " не найден"));

        ItemDtoWithComments itemDtoWithComments = itemMapper.toItemDtoWithComments(item);

        if (!Objects.equals(user.getId(), item.getOwner().getId())) {
            return itemDtoWithComments;
        } else {
            BookingDtoForView lastBookingDto
                    = bookingRepository.findFirstByItemAndStartBeforeAndStatusNotOrderByEndDesc(item,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED)
                    .map(bookingMapper::toBookingDtoForView)
                    .orElse(null);
            BookingDtoForView nextBookingDto
                    = bookingRepository.findFirstByItemAndStartAfterAndStatusNotOrderByStartAsc(item,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED)
                    .map(bookingMapper::toBookingDtoForView)
                    .orElse(null);
            itemDtoWithComments.setLastBooking(lastBookingDto);
            itemDtoWithComments.setNextBooking(nextBookingDto);
            return itemDtoWithComments;
        }
    }

    @Override
    public List<ItemDto> getAllByOwnerId(int ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + ownerId + " не найден"));
        List<Item> items = itemRepository.findAllByOwner(user);

        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = itemMapper.toItemDto(item);

            BookingDtoForView lastBookingDto
                    = bookingRepository.findFirstByItemAndStartBeforeAndStatusNotOrderByEndDesc(item,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED)
                    .map(bookingMapper::toBookingDtoForView)
                    .orElse(null);
            BookingDtoForView nextBookingDto
                    = bookingRepository.findFirstByItemAndStartAfterAndStatusNotOrderByStartAsc(item,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED)
                    .map(bookingMapper::toBookingDtoForView)
                    .orElse(null);
            itemDto.setLastBooking(lastBookingDto);
            itemDto.setNextBooking(nextBookingDto);
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    @Transactional
    @Override
    public ItemDto update(int ownerId, int id, ItemDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + ownerId + " не найден"));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item с id " + id + " не найден"));

        if (!Objects.equals(item.getOwner().getId(), owner.getId())) {
            throw new NotFoundException("пользователь с id " + ownerId + " не может получить item с id " + id);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item itemUpdated = itemRepository.save(item);

        return itemMapper.toItemDto(itemUpdated);
    }

    @Override
    public List<ItemDto> getAllByNameOrDescription(int userId, String substring) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id" + userId + " не найден"));

        if (substring.equals("")) {
            return List.of();
        }

        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(substring, substring).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto createComment(int userId, int itemId, CreatCommentDto creatCommentDto) {
        Comment comment = commentMapper.toComment(creatCommentDto);
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item с id " + itemId + " не найден"));

        bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(booker,
                        item,
                        BookingStatus.APPROVED,
                        LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("бронирование не найдено"));

        comment.setItem(item);
        comment.setUser(booker);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
