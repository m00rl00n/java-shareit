package ru.practicum.shareit.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testToDto() {

        User user = new User();
        user.setId(1);
        user.setName("Маша");
        user.setEmail("masha@example.com");
        UserDto userDto = UserDtoMapper.toDto(user);

        assertThat(userDto.getId()).isEqualTo(1);
        assertThat(userDto.getName()).isEqualTo("Маша");
        assertThat(userDto.getEmail()).isEqualTo("masha@example.com");
    }

    @Test
    void testToEntity() {

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("Даша");
        userDto.setEmail("dasha@example.com");
        User user = UserDtoMapper.toEntity(userDto);


        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Даша");
        assertThat(user.getEmail()).isEqualTo("dasha@example.com");
    }

    @Test
    void testToDtoList() {
        List<User> users = Arrays.asList(
                new User(1, "masha@example.com", "Маша"),
                new User(2, "dasha@example.com", "Даша")
        );

        List<UserDto> userDtos = UserDtoMapper.toDtoList(users);

        assertThat(userDtos).hasSize(2);
        assertThat(userDtos.get(0).getId()).isEqualTo(1);
        assertThat(userDtos.get(0).getName()).isEqualTo("Маша");
        assertThat(userDtos.get(0).getEmail()).isEqualTo("masha@example.com");
        assertThat(userDtos.get(1).getId()).isEqualTo(2);
        assertThat(userDtos.get(1).getName()).isEqualTo("Даша");
        assertThat(userDtos.get(1).getEmail()).isEqualTo("dasha@example.com");
    }
}