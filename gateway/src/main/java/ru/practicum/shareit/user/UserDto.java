package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.client.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Integer id;
    @NotBlank(groups = {Validated.Create.class})
    String name;
    @NotNull(groups = {Validated.Create.class})
    @Email(groups = {Validated.Create.class, Validated.Update.class})
    String email;
}