package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    private Item createItem(String name, String description, Boolean available, User owner, ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        return itemRepository.save(item);
    }

    private Booking createBooking(Item item, User booker) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setStatus(Status.APPROVED);
        return bookingRepository.save(booking);
    }


    @Test
    public void addTest() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);

        ItemDto result = itemService.add(itemDto, masha.getId());

        assertNotNull(result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(masha.getId(), result.getOwner().getId());
    }

    @Test
    public void updateTest() {
        User masha = createUser("Маша", "masha@yandex.ru");
        Item item = createItem("Книга", "Описание книги", true, masha, null);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Новая книга");
        itemDto.setDescription("Новое описание");
        itemDto.setAvailable(false);

        ItemDto result = itemService.update(item.getId(), itemDto, masha.getId());

        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }


    @Test
    public void getByIdTest() {
        User masha = createUser("Маша", "masha@yandex.ru");
        Item item = createItem("Книга", "Описание книги", true, masha, null);

        ItemDtoOwner result = itemService.getById(item.getId(), masha.getId());

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    public void getItemsByUserIdTest() {
        User masha = createUser("Маша", "masha@yandex.ru");
        Item item1 = createItem("Книга 1", "Описание книги 1", true, masha, null);
        Item item2 = createItem("Книга 2", "Описание книги 2", false, masha, null);

        List<ItemDtoOwner> result = itemService.getItemsByUserId(masha.getId(), 0, 10);

        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item1.getName(), result.get(0).getName());
        assertEquals(item1.getDescription(), result.get(0).getDescription());
        assertEquals(item1.getAvailable(), result.get(0).getAvailable());

        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(item2.getName(), result.get(1).getName());
        assertEquals(item2.getDescription(), result.get(1).getDescription());
        assertEquals(item2.getAvailable(), result.get(1).getAvailable());

    }

    @Test
    public void searchTest() {
        User masha = createUser("Маша", "masha@yandex.ru");
        Item item1 = createItem("Книга 1", "Описание книги 1", true, masha, null);
        Item item2 = createItem("Книга 2", "Описание книги 2", false, masha, null);

        List<ItemDto> result = itemService.search("Книга", 0, 10);

        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item1.getName(), result.get(0).getName());
        assertEquals(item1.getDescription(), result.get(0).getDescription());
        assertEquals(item1.getAvailable(), result.get(0).getAvailable());

        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(item2.getName(), result.get(1).getName());
        assertEquals(item2.getDescription(), result.get(1).getDescription());
        assertEquals(item2.getAvailable(), result.get(1).getAvailable());
    }

    @Test
    public void validateTest_ValidData() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);

        assertDoesNotThrow(() -> itemService.add(itemDto, masha.getId()));
    }

    @Test
    public void validateTest_NullUserId_ThrowsNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);

        assertThrows(NotFoundException.class, () -> itemService.add(itemDto, null));
    }

    @Test
    public void validateTest_EmptyName_ThrowsValidationException() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemService.add(itemDto, masha.getId()));
    }

    @Test
    public void validateTest_NullDescription_ThrowsValidationException() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription(null);
        itemDto.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemService.add(itemDto, masha.getId()));
    }

    @Test
    public void validateTest_NullAvailability_ThrowsValidationException() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(null);

        assertThrows(ValidationException.class, () -> itemService.add(itemDto, masha.getId()));
    }

    @Test
    public void commentItemTest_ThrowsValidationException_NoActiveBooking() {
        User masha = createUser("Маша", "masha@yandex.ru");
        Item item = createItem("Книга", "Описание книги", true, masha, null);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Отличная книга!");
        assertThrows(ValidationException.class,
                () -> itemService.commentItem(item.getId(), commentDto, masha.getId()));
    }

    @Test
    public void addTest_WithNonExistentRequestId_ThrowsNotFoundException() {
        User masha = createUser("Маша", "masha@yandex.ru");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Книга");
        itemDto.setDescription("Описание книги");
        itemDto.setAvailable(true);
        itemDto.setRequestId(999);

        assertThrows(NotFoundException.class, () -> itemService.add(itemDto, masha.getId()));
    }

    @Test
    public void updateTest_WithDifferentOwner_ThrowsNotFoundException() {
        User masha = createUser("Маша", "masha@yandex.ru");
        User vasya = createUser("Вася", "vasya@mail.ru");

        Item item = createItem("Книга", "Описание книги", true, masha, null);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Новая книга");
        itemDto.setDescription("Новое описание");
        itemDto.setAvailable(false);

        assertThrows(NotFoundException.class, () -> itemService.update(item.getId(), itemDto, vasya.getId()));
    }
}



