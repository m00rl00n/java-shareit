package ru.practicum.shareit.booking;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exception.InvalidException;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Future;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer id;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Integer itemId;
    private UserDto booker;
    private Status status;

    public static void validate(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new InvalidException("Нельзя создать бронь без указания времени начала или окончания");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new InvalidException("Бронь не может закончиться раньше ее начала");
        }
    }
}