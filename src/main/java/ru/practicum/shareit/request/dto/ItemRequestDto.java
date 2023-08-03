package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Integer id;
    String description;
    User requestor;
}
