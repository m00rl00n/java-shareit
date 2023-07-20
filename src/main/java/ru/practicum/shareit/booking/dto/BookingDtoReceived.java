package ru.practicum.shareit.booking.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoReceived {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
    User booker;
    Status status;
}