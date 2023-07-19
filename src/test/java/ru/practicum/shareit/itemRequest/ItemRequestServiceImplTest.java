package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemRequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestMapper requestMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create() {
        int userId = 1;
        NewRequestDto creationRequestDto = new NewRequestDto();
        User requestor = new User();
        requestor.setId(userId);

        ItemRequest request = new ItemRequest();
        request.setId(1);
        ItemRequestDto expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestMapper.toRequest(creationRequestDto, requestor)).thenReturn(request);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toRequestDto(request)).thenReturn(expectedItemRequestDto);

        ItemRequestDto createdItemRequestDto = itemRequestService.create(userId, creationRequestDto);

        assertEquals(expectedItemRequestDto.getId(), createdItemRequestDto.getId());
    }

    @Test
    public void getById() {
        int userId = 1;
        int requestId = 1;
        User requestor = new User();
        requestor.setId(userId);
        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        ItemRequestDto expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(requestId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(requestMapper.toRequestDto(request)).thenReturn(expectedItemRequestDto);

        ItemRequestDto result = itemRequestService.getById(userId, requestId);

        assertEquals(expectedItemRequestDto.getId(), result.getId());
    }

    @Test
    public void getAllByUser() {
        int userId = 1;
        User requestor = new User();
        requestor.setId(userId);
        List<ItemRequest> itemRequests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest();
        request1.setId(1);
        itemRequests.add(request1);
        ItemRequest request2 = new ItemRequest();
        request2.setId(2);
        itemRequests.add(request2);
        List<ItemRequestDto> expectedItemRequestDtos = new ArrayList<>();
        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(1);
        expectedItemRequestDtos.add(itemRequestDto1);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2);
        expectedItemRequestDtos.add(itemRequestDto2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestRepository.findAllByRequestor(requestor, Sort.by(Sort.Direction.DESC, "created"))).thenReturn(itemRequests);
        when(requestMapper.toRequestDto(request1)).thenReturn(itemRequestDto1);
        when(requestMapper.toRequestDto(request2)).thenReturn(itemRequestDto2);

        List<ItemRequestDto> result = itemRequestService.getAllByUser(userId);

        assertEquals(expectedItemRequestDtos.size(), result.size());
    }
}

