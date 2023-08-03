package ru.practicum.shareit.Booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingDtoReturned;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@ExtendWith(SpringExtension.class)
public class BookingServiceImplTest {

    private static final Integer FROM = 0;
    private static final Integer SIZE = 10;
    private static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    void createBookingTest() {
        User owner = createUser(1, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        User user = createUser(1, "Маша2", "masha2@yandex.ru");
        ;
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        BookingDtoReceived bookingDto = mock(BookingDtoReceived.class);

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, user.getId()));

        verify(itemRepository).findById(anyInt());
        verify(userRepository).findById(anyInt());
        verify(bookingDto).getItemId();
    }

    @Test
    void bookingConfirmationTest() {
        User owner = createUser(1, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, new User(), WAITING
        );
        BookingDtoReturned bookingDto = BookingDtoMapper.toBookingDtoReturned(booking);

        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booking.getItem().getOwner()));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDtoReturned result = bookingService.bookingConfirmation(booking.getId(), true, owner.getId());
        result.getStart().format(DateTimeFormatter.ofPattern(TIME_PATTERN));

        verify(bookingRepository, times(1)).findById(eq(booking.getId()));
        verify(userRepository, times(1)).findById(eq(owner.getId()));
        verify(bookingRepository, times(1)).save(any(Booking.class));

        assertNotNull(result);
        assertEquals(bookingDto.getId(), result.getId());
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
        assertEquals(bookingDto.getItem(), result.getItem());
        assertEquals(bookingDto.getBooker(), result.getBooker());
        assertEquals(APPROVED, result.getStatus());
    }

    @Test
    public void bookingConfirmationWrongUserTest() {
        User owner = createUser(2, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, new User(), APPROVED
        );

        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                bookingService.bookingConfirmation(booking.getId(), true, owner.getId()));

        verify(bookingRepository, times(1)).findById(eq(booking.getId()));
        verify(userRepository, times(1)).findById(eq(owner.getId()));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    public void bookingConfirmationDoubleApprovedTest() {
        User owner = createUser(2, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, new User(), APPROVED
        );

        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booking.getItem().getOwner()));

        assertThrows(ValidationException.class, () ->
                bookingService.bookingConfirmation(booking.getId(), true, owner.getId()));

        verify(bookingRepository, times(1)).findById(eq(booking.getId()));
        verify(userRepository, times(1)).findById(eq(owner.getId()));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    public void getBookingByIdTest() {
        User owner = createUser(1, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        User booker = createUser(1, "Маша3", "masha3@yandex.ru");
        ;

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, WAITING
        );
        BookingDtoReturned bookingDto = BookingDtoMapper.toBookingDtoReturned(booking);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booking.getBooker()));
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));

        BookingDtoReturned result = bookingService.getBookingById(booking.getId(), booker.getId());

        verify(userRepository, times(1)).findById(eq(booker.getId()));
        verify(bookingRepository, times(1)).findById(eq(booking.getId()));

        assertNotNull(result);
        assertEquals(bookingDto.getId(), result.getId());
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
        assertEquals(bookingDto.getItem(), result.getItem());
        assertEquals(bookingDto.getBooker(), result.getBooker());
        assertEquals(bookingDto.getStatus(), result.getStatus());
    }

    @Test
    public void getBookingByIdWrongUserTest() {
        User owner = createUser(1, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        User booker = createUser(1, "Маша2", "masha2@yandex.ru");
        ;

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, WAITING
        );

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(), booker.getId()));

        verify(userRepository, times(1)).findById(eq(booker.getId()));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    public void getBookingByWrongIdTest() {
        User owner = createUser(1, "Маша", "masha@yandex.ru");
        ;

        Item item = createItem(1, "Книга", "Описание книги", owner,
                new ItemRequest(), true);

        User booker = createUser(1, "Маша2", "masha2@yandex.ru");
        ;

        Booking booking = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, WAITING
        );

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(), booker.getId()));

        verify(userRepository, times(1)).findById(eq(booker.getId()));
        verify(bookingRepository, times(1)).findById(eq(booking.getId()));
    }

    @Test
    public void getAllBookingsByUserTest() {

        User user = createUser(1, "Маша", "masha@yandex.ru");
        ;
        when(userRepository.findById(1)).thenReturn(Optional.of(user));


        User owner1 = createUser(1, "Маша1", "masha1@yandex.ru");
        ;
        User owner2 = createUser(1, "Маша2", "masha2@yandex.ru");
        ;

        Booking booking1 = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), owner1, WAITING
        );
        booking1.getItem().setId(1);
        BookingDtoReturned bookingDto1 = BookingDtoMapper.toBookingDtoReturned(booking1);

        Booking booking2 = createBooking(
                2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), owner2, APPROVED
        );
        booking2.getItem().setId(2);
        BookingDtoReturned bookingDto2 = BookingDtoMapper.toBookingDtoReturned(booking2);

        List<Booking> bookingsDB = new ArrayList<>();
        bookingsDB.add(booking1);
        bookingsDB.add(booking2);


        Pageable pageable = PageRequest.of(FROM / SIZE, SIZE, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingPage = new PageImpl<>(bookingsDB, pageable, bookingsDB.size());
        when(bookingRepository.findAllByBooker(eq(user), any(Pageable.class))).thenReturn(bookingPage);

        List<BookingDtoReturned> result = bookingService.getAllBookingsByUser("ALL", user.getId(), FROM, SIZE);

        verify(userRepository, times(1)).findById(eq(user.getId()));
        verify(bookingRepository, times(1)).findAllByBooker(eq(user), eq(pageable));

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(bookingDto1.getId(), result.get(0).getId());
        assertEquals(bookingDto1.getStart(), result.get(0).getStart());
        assertEquals(bookingDto1.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDto1.getItem(), result.get(0).getItem());
        assertEquals(bookingDto1.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDto1.getStatus(), result.get(0).getStatus());

        assertEquals(bookingDto2.getId(), result.get(1).getId());
        assertEquals(bookingDto2.getStart(), result.get(1).getStart());
        assertEquals(bookingDto2.getEnd(), result.get(1).getEnd());
        assertEquals(bookingDto2.getItem(), result.get(1).getItem());
        assertEquals(bookingDto2.getBooker(), result.get(1).getBooker());
        assertEquals(bookingDto2.getStatus(), result.get(1).getStatus());
    }

    @Test
    public void getAllBookingsByOwnerTest() {

        User user = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User owner1 = createUser(1, "Маша1", "masha1@yandex.ru");
        ;
        User owner2 = createUser(1, "Маша2", "masha2@yandex.ru");
        ;

        Booking booking1 = createBooking(
                1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), owner1, WAITING
        );
        booking1.getItem().setId(1);
        BookingDtoReturned bookingDto1 = BookingDtoMapper.toBookingDtoReturned(booking1);

        Booking booking2 = createBooking(
                2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), owner2, APPROVED
        );
        booking2.getItem().setId(2);
        BookingDtoReturned bookingDto2 = BookingDtoMapper.toBookingDtoReturned(booking2);

        List<Booking> bookingsDB = new ArrayList<>();
        bookingsDB.add(booking1);
        bookingsDB.add(booking2);

        Pageable pageable = PageRequest.of(FROM / SIZE, SIZE, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingPage = new PageImpl<>(bookingsDB, pageable, bookingsDB.size());
        when(bookingRepository.findAllByItemOwner(eq(user), any(Pageable.class)))
                .thenReturn(bookingPage);

        List<BookingDtoReturned> result = bookingService.getAllBookingsByOwner("ALL", user.getId(), FROM, SIZE);

        verify(userRepository, times(1)).findById(eq(1));
        verify(bookingRepository, times(1)).findAllByItemOwner(eq(user), eq(pageable));

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(bookingDto1.getId(), result.get(0).getId());
        assertEquals(bookingDto1.getStart(), result.get(0).getStart());
        assertEquals(bookingDto1.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDto1.getItem(), result.get(0).getItem());
        assertEquals(bookingDto1.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDto1.getStatus(), result.get(0).getStatus());

        assertEquals(bookingDto2.getId(), result.get(1).getId());
        assertEquals(bookingDto2.getStart(), result.get(1).getStart());
        assertEquals(bookingDto2.getEnd(), result.get(1).getEnd());
        assertEquals(bookingDto2.getItem(), result.get(1).getItem());
        assertEquals(bookingDto2.getBooker(), result.get(1).getBooker());
        assertEquals(bookingDto2.getStatus(), result.get(1).getStatus());
    }

    private User createUser(Integer id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private Item createItem(Integer id, String name, String description, User owner, ItemRequest itemRequest,
                            Boolean available) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        item.setAvailable(available);
        return item;
    }

    private Booking createBooking(Integer id, LocalDateTime start, LocalDateTime end, Item item, User booker,
                                  Status status) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return booking;
    }
}