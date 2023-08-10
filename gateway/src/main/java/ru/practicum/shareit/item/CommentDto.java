package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Integer id;
    @NotEmpty
    String text;
    ItemDto item;
    UserDto author;
    LocalDateTime created;
}