package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.client.Validated;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(groups = {Validated.Create.class})
    private String name;
    @NotBlank(groups = {Validated.Create.class})
    private String description;
    @NotNull(groups = {Validated.Create.class})
    private Boolean available;
    private UserDto owner;
    private Integer requestId;
}
