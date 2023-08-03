package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOwner {
    Integer id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
}
