package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
    Integer bookerId;
    Status status;
}