package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exeption.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper requestMapper;

    @Transactional
    @Override
    public ItemRequestDto create(int userId, NewRequestDto creationRequestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));
        ItemRequest request = requestMapper.toRequest(creationRequestDto, requestor);

        ItemRequest requestCreated = requestRepository.save(request);

        log.info("создан новый запрос: {}", requestCreated);
        return requestMapper.toRequestDto(requestCreated);
    }

    @Override
    public ItemRequestDto getById(int userId, int requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("запрос не найден"));

        return requestMapper.toRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getAllByUser(int userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));

        Sort sort = Sort.sort(ItemRequest.class).by(ItemRequest::getCreated).descending();

        return requestRepository.findAllByRequestor(requestor, sort).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAll(int userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));

        Sort sort = Sort.sort(ItemRequest.class).by(ItemRequest::getCreated).descending();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        return requestRepository.findAll(pageRequest).stream()
                .filter(request -> request.getRequestor().getId() != userId)
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}