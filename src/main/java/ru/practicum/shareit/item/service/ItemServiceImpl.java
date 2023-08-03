package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.model.Status.APPROVED;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final CommentRepository commentRepository;
    final RequestRepository requestRepository;

    @Override
    public ItemDto add(ItemDto itemDto, Integer id) {
        validate(itemDto, id);
        log.info("Добавление вещи");
        User user = getUserById(id);
        Item item = ItemDtoMapper.toItem(itemDto, user, null);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException("Запрос с айди " + itemDto.getRequestId() + " не найден"));
            item.setRequest(itemRequest);
        }
        return ItemDtoMapper.toItemDto(itemRepository.save(item));
    }

    private User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }


    @Override
    public ItemDto update(Integer itemId, ItemDto itemDto, Integer userId) {
        Item item = getItemById(itemId);
        log.info("Обновление вещи");
        validateOwnership(item, userId);
        updateItemFields(item, itemDto);
        return ItemDtoMapper.toItemDto(itemRepository.save(item));
    }

    private Item getItemById(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    private void validateOwnership(Item item, Integer userId) {
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец вещи другой пользователь");
        }
    }

    private void updateItemFields(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }

    @Override
    public CommentDto commentItem(Integer itemId, CommentDto commentDto, Integer authorId) {
        validateCommentText(commentDto);
        log.info("Добавление нового коментария");
        Item item = getItemById(itemId);
        User user = getUserById(authorId);
        validateBookingsByUserAndItem(user, item);
        validateDuplicateCommentByUserAndItem(user, item);
        Comment comment = createComment(commentDto, user, item);
        return CommentDtoMapper.toCommentDto(commentRepository.save(comment));
    }

    private void validateCommentText(CommentDto commentDto) {
        if (commentDto.getText().isBlank() || commentDto.getText().isEmpty()) {
            throw new ValidationException("Нельзя оставить пустой комментарий о вещи");
        }
    }

    private void validateBookingsByUserAndItem(User user, Item item) {
        List<Booking> bookings = bookingRepository.findBookingsByBookerIdAndItemId(user.getId(), item.getId());
        boolean hasApprovedBookings = false;
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (booking.getEnd().isBefore(now) && booking.getStatus().equals(APPROVED)) {
                hasApprovedBookings = true;
                break;
            }
        }
        if (!hasApprovedBookings) {
            throw new ValidationException("Пользователь не бронировал вещь");
        }
    }

    private void validateDuplicateCommentByUserAndItem(User user, Item item) {
        List<Comment> comments = commentRepository.findCommentByItemId(item.getId());
        for (Comment comment : comments) {
            if (comment.getAuthor().getId().equals(user.getId())) {
                throw new ValidationException("Комментарий уже оставлен");
            }
        }
    }

    private Comment createComment(CommentDto commentDto, User user, Item item) {
        return CommentDtoMapper.toComment(commentDto, user, item);
    }

    @Override
    public ItemDtoOwner getById(Integer itemId, Integer userId) {
        Item item = getItemById(itemId);
        log.info("Получение вещи");
        ItemDtoOwner itemDtoOwner = ItemDtoMapper.toItemDtoOwner(item);
        List<CommentDto> comments = getCommentsByItemId(itemId);
        itemDtoOwner.setComments(comments);
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = getLastBookingByItemId(itemId);
            Booking nextBooking = getNextBookingByItemId(itemId);
            if (lastBooking != null) {
                itemDtoOwner.setLastBooking(BookingDtoMapper.toBookingDto(lastBooking));
            }
            if (nextBooking != null) {
                itemDtoOwner.setNextBooking(BookingDtoMapper.toBookingDto(nextBooking));
            }
        }
        return itemDtoOwner;
    }

    private List<CommentDto> getCommentsByItemId(Integer itemId) {
        List<Comment> comments = commentRepository.findCommentByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(CommentDtoMapper.toCommentDto(comment));
        }
        return commentDtos;
    }

    private Booking getLastBookingByItemId(Integer itemId) {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findFirstByItemIdAndStartIsBeforeAndStatusIsOrderByStartDesc(
                itemId, now, APPROVED);
    }

    private Booking getNextBookingByItemId(Integer itemId) {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findTopByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(
                itemId, now, APPROVED);
    }

    @Override
    public List<ItemDtoOwner> getItemsByUserId(Integer userId, Integer from, Integer size) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Получение вещей пользователя с пагинацией");
        List<Booking> lastBookings = getLastBookingsByStatusAndEndIsBefore(String.valueOf(APPROVED), now);
        List<Booking> nextBookings = getNextBookingsByStatusAndStartIsAfter(String.valueOf(APPROVED), now);
        return getItemDtoOwnersByOwnerIdWithPagination(userId, from, size, lastBookings, nextBookings);
    }

    private List<ItemDtoOwner> getItemDtoOwnersByOwnerIdWithPagination(Integer ownerId, Integer from, Integer size, List<Booking> lastBookings, List<Booking> nextBookings) {
        List<ItemDtoOwner> items = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Item> ownerItems = getItemsByOwnerIdOrderByIdAsc(ownerId, pageRequest);
        for (Item item : ownerItems) {
            ItemDtoOwner itemDtoOwner = ItemDtoMapper.toItemDtoOwner(item);
            Booking lastBooking = getLastBookingByItem(item.getId(), lastBookings);
            Booking nextBooking = getNextBookingByItem(item.getId(), nextBookings);
            if (lastBooking != null) {
                itemDtoOwner.setLastBooking(BookingDtoMapper.toBookingDto(lastBooking));
            }
            if (nextBooking != null) {
                itemDtoOwner.setNextBooking(BookingDtoMapper.toBookingDto(nextBooking));
            }
            items.add(itemDtoOwner);
        }
        return items;
    }


    private List<Booking> getLastBookingsByStatusAndEndIsBefore(String status, LocalDateTime endDateTime) {
        return bookingRepository.findBookingsByStatusAndEndIsBeforeOrderByStartDesc(Status.valueOf(status),
                endDateTime);
    }

    private List<Booking> getNextBookingsByStatusAndStartIsAfter(String status, LocalDateTime startDateTime) {
        return bookingRepository.findBookingsByStatusAndStartIsAfterOrderByStartAsc(Status.valueOf(status),
                startDateTime);
    }


    private List<Item> getItemsByOwnerIdOrderByIdAsc(Integer ownerId, PageRequest pageRequest) {
        return itemRepository.findByOwnerIdOrderByIdAsc(ownerId, pageRequest);
    }

    private Booking getLastBookingByItem(Integer itemId, List<Booking> lastBookings) {
        for (Booking booking : lastBookings) {
            if (booking.getItem().getId().equals(itemId)) {
                return booking;
            }
        }
        return null;
    }

    private Booking getNextBookingByItem(Integer itemId, List<Booking> nextBookings) {
        for (Booking booking : nextBookings) {
            if (booking.getItem().getId().equals(itemId)) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {
        log.info("Поиск вещей содержащих ");

        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            List<Item> searchResults = itemRepository.search(text, PageRequest.of(from / size, size));
            List<ItemDto> itemDtos = new ArrayList<>();
            for (Item item : searchResults) {
                itemDtos.add(ItemDtoMapper.toItemDto(item));
            }
            return itemDtos;
        }
    }

    private void validate(ItemDto itemDto, Integer userId) {
        if (userId == null) {
            throw new NotFoundException("Невозможно создать вещь не указывая ее владельца");
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ValidationException("Название вещи не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ValidationException("Описание вещи не может быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Необходимо указать доступность");
        }
    }
}

