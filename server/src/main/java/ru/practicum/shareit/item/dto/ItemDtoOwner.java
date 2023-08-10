package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOwner {
    Integer id;
    String name;
    String description;
    Boolean available;
    Integer requestId;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
}
