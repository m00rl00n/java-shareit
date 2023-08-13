package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exception.InvalidException;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Integer id;
    @FutureOrPresent
    LocalDateTime start;
    @Future
    LocalDateTime end;
    Integer itemId;
    UserDto booker;
    Status status;

    public static void validate(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new InvalidException("Нельзя создать бронь без указания времени");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new InvalidException("Бронь должна быть создана до ее начала");
        }
    }
}