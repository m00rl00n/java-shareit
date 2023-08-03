package ru.practicum.shareit.request.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {
    final RequestRepository requestRepository;
    final UserRepository userRepository;
    final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId) {
        User user = getUserById(userId);
        validateRequestDescription(itemRequestDto.getDescription());
        log.info("Создание запроса");
        ItemRequest itemRequest = requestRepository.save(RequestDtoMapper.toItemRequest(itemRequestDto, user));
        return RequestDtoMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Integer userId) {
        User user = getUserById(userId);
        log.info("Просмотр запросов");
        List<ItemRequestDto> itemRequestDtos = getRequestDtosByRequestorId(user.getId());
        List<Item> items = itemRepository.findAll();
        return searchItemsForRequests(itemRequestDtos, items);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size) {
        User user = getUserById(userId);
        log.info("Просмотр всех запросов");
        List<ItemRequestDto> requestDtos = getRequestDtosByRequestorIdNotOrderByCreatedAsc(user.getId(), from, size);
        List<Item> items = itemRepository.findAll();
        return searchItemsForRequests(requestDtos, items);
    }

    @Override
    public ItemRequestDto getRequestById(Integer userId, Integer requestId) {
        User user = getUserById(userId);
        ItemRequest itemRequest = getRequestById(requestId);
        log.info("Просмотр запроса с айди" + requestId + " пользователем с айди " + userId);
        ItemRequestDto itemRequestDto = RequestDtoMapper.toItemRequestDto(itemRequest);
        List<ItemDtoOwner> items = getItemsByRequestId(requestId);
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с айди " + userId + " не найден"));
    }

    private void validateRequestDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Запрос не может быть пустым");
        }
    }

    private List<ItemRequestDto> getRequestDtosByRequestorId(Integer requestorId) {
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorIdOrderByCreatedAsc(requestorId);
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            itemRequestDtos.add(RequestDtoMapper.toItemRequestDto(request));
        }
        return itemRequestDtos;
    }

    private List<ItemRequestDto> getRequestDtosByRequestorIdNotOrderByCreatedAsc(Integer userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests = requestRepository
                .findItemRequestsByRequestorIdNotOrderByCreatedAsc(userId, pageable);
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            itemRequestDtos.add(RequestDtoMapper.toItemRequestDto(request));
        }
        return itemRequestDtos;
    }

    private ItemRequest getRequestById(Integer requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с айди " + requestId + " не найден"));
    }

    private List<ItemDtoOwner> getItemsByRequestId(Integer requestId) {
        List<Item> items = itemRepository.findItemsByRequestId(requestId);
        List<ItemDtoOwner> itemDtoOwners = new ArrayList<>();
        for (Item item : items) {
            if (item.getRequest() != null && item.getRequest().getId().equals(requestId)) {
                itemDtoOwners.add(ItemDtoMapper.toItemDtoOwner(item));
            }
        }
        return itemDtoOwners;
    }

    private List<ItemRequestDto> searchItemsForRequests(List<ItemRequestDto> requestDtos, List<Item> items) {
        for (ItemRequestDto request : requestDtos) {
            List<ItemDtoOwner> answers = new ArrayList<>();
            for (Item item : items) {
                if (item.getRequest() != null && item.getRequest().getId().equals(request.getId())) {
                    answers.add(ItemDtoMapper.toItemDtoOwner(item));
                }
            }
            request.setItems(answers);
        }
        return requestDtos;
    }
}

