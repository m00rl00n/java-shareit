package ru.practicum.shareit.request;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class
RequestServiceImplTest {

    private static final Integer FROM = 0;
    private static final Integer SIZE = 10;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void createRequestTest() {
        User requestor = createUser(1, "маша", "маша@yandex.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(requestor));

        ItemRequest itemRequest = createItemRequest(1, "Request", requestor, LocalDateTime.now());
        ItemRequestDto itemRequestDto = RequestDtoMapper.toItemRequestDto(itemRequest);

        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = requestService.create(itemRequestDto, requestor.getId());

        verify(userRepository).findById(requestor.getId());
        verify(requestRepository).save(any(ItemRequest.class));

        assertNotNull(result);
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        assertEquals(itemRequest.getRequestor().getId(), result.getRequestorId());
    }

    @Test
    void getUserRequestsTest() {
        User requestor = createUser(1, "маша", "маша@yandex.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest itemRequest1 = createItemRequest(1, "Request1", requestor, LocalDateTime.now());
        ItemRequest itemRequest2 = createItemRequest(2, "Request2", requestor, LocalDateTime.now());
        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);

        List<ItemRequestDto> itemRequestDtos = itemRequests.stream()
                .map(RequestDtoMapper::toItemRequestDto)
                .collect(Collectors.toList());

        when(requestRepository.findAllByRequestorIdOrderByCreatedAsc(requestor.getId())).thenReturn(itemRequests);

        List<Item> items = Collections.emptyList();
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> result = requestService.getUserRequests(requestor.getId());

        verify(userRepository).findById(requestor.getId());
        verify(requestRepository).findAllByRequestorIdOrderByCreatedAsc(requestor.getId());
        verify(itemRepository).findAll();

        assertNotNull(result);
        assertEquals(itemRequestDtos.size(), result.size());
    }

    @Test
    void getAllRequestsTest() {
        User requestor = createUser(1, "маша", "маша@yandex.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest itemRequest1 = createItemRequest(1, "Request1", requestor, LocalDateTime.now());
        ItemRequest itemRequest2 = createItemRequest(2, "Request2", requestor, LocalDateTime.now());
        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);

        List<ItemRequestDto> itemRequestDtos = itemRequests.stream()
                .map(RequestDtoMapper::toItemRequestDto)
                .collect(Collectors.toList());

        when(requestRepository.findItemRequestsByRequestorIdNotOrderByCreatedAsc(eq(requestor.getId()),
                any(Pageable.class))).thenReturn(itemRequests);

        List<Item> items = Collections.emptyList();
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> result = requestService.getAllRequests(requestor.getId(), FROM, SIZE);

        verify(userRepository).findById(requestor.getId());
        verify(requestRepository)
                .findItemRequestsByRequestorIdNotOrderByCreatedAsc(eq(requestor.getId()), any(Pageable.class));
        verify(itemRepository).findAll();

        assertNotNull(result);
        assertEquals(itemRequestDtos.size(), result.size());
    }

    @Test
    void getRequestByIdTest() {
        User requestor = createUser(1, "маша", "маша@yandex.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest itemRequest = createItemRequest(1, "Request", requestor, LocalDateTime.now());
        ItemRequestDto itemRequestDto = RequestDtoMapper.toItemRequestDto(itemRequest);

        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        List<Item> items = new ArrayList<>();
        when(itemRepository.findItemsByRequestId(requestor.getId())).thenReturn(items);

        List<ItemDtoOwner> itemsDto = new ArrayList<>();
        itemRequestDto.setItems(itemsDto);

        ItemRequestDto result = requestService.getRequestById(requestor.getId(), itemRequest.getId());
        result.setItems(itemsDto);

        verify(userRepository).findById(requestor.getId());
        verify(requestRepository).findById(itemRequest.getId());
        verify(itemRepository).findItemsByRequestId(itemRequest.getId());

        assertNotNull(result);
        assertEquals(itemRequestDto.getId(), result.getId());
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        assertEquals(itemRequestDto.getRequestorId(), result.getRequestorId());
        assertEquals(itemRequestDto.getCreated(), result.getCreated());
        assertEquals(itemRequestDto.getItems().size(), result.getItems().size());
    }


    private User createUser(Integer id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private ItemRequest createItemRequest(Integer id, String description, User user, LocalDateTime ldt) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        itemRequest.setDescription(description);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(ldt);
        return itemRequest;
    }
}