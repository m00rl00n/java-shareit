package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.client.Validated;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Integer id;
    @NotBlank(groups = {Validated.Create.class})
    String name;
    @NotBlank(groups = {Validated.Create.class})
    String description;
    @NotNull(groups = {Validated.Create.class})
    Boolean available;
    UserDto owner;
    Integer requestId;
}
