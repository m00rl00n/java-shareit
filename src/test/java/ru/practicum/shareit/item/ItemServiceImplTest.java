package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.Status.APPROVED;

@ExtendWith(SpringExtension.class)
public class ItemServiceImplTest {
    private static final Integer FROM = 0;
    private static final Integer SIZE = 10;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;


    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void createItemTest() {
        User masha = createUser(1, "Маша", "masha@yandex.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(2);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);
        itemDto.setOwner(masha);
        itemDto.setRequestId(itemRequest.getId());

        when(userRepository.findById(masha.getId())).thenReturn(Optional.of(masha));
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(createItemWithId(itemDto, 1));

        ItemDto result = itemService.add(itemDto, masha.getId());

        verify(userRepository).findById(masha.getId());
        verify(requestRepository).findById(itemRequest.getId());
        verify(itemRepository).save(any(Item.class));

        assertNotNull(result.getId());
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getOwner().getId(), result.getOwner().getId());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
    }

    @Test
    void updateItemTest() {

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item = createItem(2, "Книга", "Описание книги",
                true, masha, new ItemRequest());
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2);
        itemDto.setName("Новая книга");
        itemDto.setDescription("Новое описание");
        itemDto.setAvailable(true);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto updatedItem = itemService.update(item.getId(), itemDto, masha.getId());


        verify(itemRepository).findById(item.getId());
        verify(itemRepository).save(any(Item.class));

        assertEquals(itemDto.getId(), updatedItem.getId());
        assertEquals(itemDto.getName(), updatedItem.getName());
        assertEquals(itemDto.getDescription(), updatedItem.getDescription());
        assertEquals(itemDto.getAvailable(), updatedItem.getAvailable());
    }


    @Test
    void getItemByIdTest() {

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item = createItem(2, "Книга", "Описание книги",
                true, masha, new ItemRequest());

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));


        ItemDtoOwner retrievedItem = itemService.getById(item.getId(), masha.getId());


        verify(itemRepository).findById(item.getId());

        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getDescription(), retrievedItem.getDescription());
        assertEquals(item.getAvailable(), retrievedItem.getAvailable());
    }

    @Test
    void commentWrongItemTest() {

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item = createItem(1, "Книга", "Описание книги",
                true, masha, new ItemRequest());


        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Комментарий");


        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.commentItem(item.getId(), commentDto, masha.getId()));

        assertEquals("Вещь не найдена", exception.getMessage());

        verify(itemRepository).findById(item.getId());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(commentRepository);
    }

    @Test
    void commentItemWrongUserTest() {

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item = createItem(1, "Книга", "Описание книги",
                true, masha, new ItemRequest());


        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(masha.getId())).thenReturn(Optional.empty());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Комментарий");


        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.commentItem(item.getId(), commentDto, masha.getId()));

        assertEquals("Пользователь не найден", exception.getMessage());

        verify(itemRepository).findById(item.getId());
        verify(userRepository).findById(masha.getId());
        verifyNoInteractions(commentRepository);
    }

    @Test
    void commentItemWithoutBookingTest() {

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item = createItem(1, "Книга", "Описание книги",
                true, masha, new ItemRequest());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Комментарий");

        List<Booking> bookings = new ArrayList<>();


        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(masha.getId())).thenReturn(Optional.of(masha));
        when(bookingRepository.findBookingsByBookerIdAndItemId(masha.getId(), item.getId())).thenReturn(bookings);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.commentItem(item.getId(), commentDto, masha.getId()));

        assertEquals("Пользователь не бронировал вещь", exception.getMessage());

        verify(itemRepository).findById(item.getId());
        verify(userRepository).findById(masha.getId());
        verify(bookingRepository).findBookingsByBookerIdAndItemId(masha.getId(), item.getId());
        verifyNoInteractions(commentRepository);
    }

    @Test
    void viewAllItemsTest() {

        List<Booking> lastBookings = new ArrayList<>();
        List<Booking> nextBookings = new ArrayList<>();

        User masha = createUser(1, "Маша", "masha@yandex.ru");
        Item item1 = createItem(1, "Книга1", "Описание книги1",
                true, masha, new ItemRequest());
        Item item2 = createItem(2, "Книга2", "Описание книги2",
                true, masha, new ItemRequest());

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);


        when(bookingRepository.findBookingsByStatusAndEndIsBeforeOrderByStartDesc(eq(APPROVED),
                any(LocalDateTime.class))).thenReturn(lastBookings);
        when(bookingRepository.findBookingsByStatusAndStartIsAfterOrderByStartAsc(eq(APPROVED),
                any(LocalDateTime.class))).thenReturn(nextBookings);
        when(itemRepository.findByOwnerIdOrderByIdAsc(eq(1), any(PageRequest.class))).thenReturn(items);


        List<ItemDtoOwner> result = itemService.getItemsByUserId(masha.getId(), FROM, SIZE);


        verify(bookingRepository).findBookingsByStatusAndEndIsBeforeOrderByStartDesc(eq(APPROVED),
                any(LocalDateTime.class));
        verify(bookingRepository).findBookingsByStatusAndStartIsAfterOrderByStartAsc(eq(APPROVED),
                any(LocalDateTime.class));
        verify(itemRepository).findByOwnerIdOrderByIdAsc(eq(1), any(PageRequest.class));

        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item1.getName(), result.get(0).getName());
        assertEquals(item1.getDescription(), result.get(0).getDescription());
        assertNull(result.get(0).getLastBooking());
        assertNull(result.get(0).getNextBooking());

        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(item2.getName(), result.get(1).getName());
        assertEquals(item2.getDescription(), result.get(1).getDescription());
        assertNull(result.get(1).getLastBooking());
        assertNull(result.get(1).getNextBooking());
    }

    @Test
    void searchItemsTest() {

        String searchText = "Text";

        Item item1 = createItem(1, "Книга1", "Описание книги1",
                true, new User(), new ItemRequest());
        Item item2 = createItem(2, "Книга2", "Описание книги2",
                true, new User(), new ItemRequest());

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);


        when(itemRepository.search(searchText, PageRequest.of(FROM / SIZE, SIZE))).thenReturn(items);

        List<ItemDto> result = itemService.search(searchText, FROM, SIZE);


        verify(itemRepository).search(searchText, PageRequest.of(FROM / SIZE, SIZE));

        assertEquals(2, result.size());
        assertEquals(items.get(0).getId(), result.get(0).getId());
        assertEquals(items.get(0).getName(), result.get(0).getName());
        assertEquals(items.get(0).getDescription(), result.get(0).getDescription());

        assertEquals(items.get(1).getId(), result.get(1).getId());
        assertEquals(items.get(1).getName(), result.get(1).getName());
        assertEquals(items.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    void searchItemsBlankTextTest() {

        List<ItemDto> result = itemService.search("", FROM, SIZE);


        verifyNoInteractions(itemRepository);

        assertTrue(result.isEmpty());
    }

    private Item createItemWithId(ItemDto itemDto, Integer itemId) {
        User owner = itemDto.getOwner();
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = new ItemRequest();
            itemRequest.setId(itemDto.getRequestId());
        }

        return createItem(itemId, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), owner,
                itemRequest);
    }

    private User createUser(Integer id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private Item createItem(Integer id, String name, String description, Boolean available, User owner,
                            ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        return item;
    }
}