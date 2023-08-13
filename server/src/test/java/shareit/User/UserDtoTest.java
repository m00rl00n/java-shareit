package shareit.User;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDtoTest {

    @Test
    public void testGetterAndSetter() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("Маша");
        userDto.setEmail("masha@example.com");

        assertEquals(1, userDto.getId());
        assertEquals("Маша", userDto.getName());
        assertEquals("masha@example.com", userDto.getEmail());
    }

    @Test
    public void testNoArgsConstructor() {
        UserDto userDto = new UserDto();
        assertEquals(null, userDto.getId());
        assertEquals(null, userDto.getName());
        assertEquals(null, userDto.getEmail());
    }

    @Test
    public void testAllArgsConstructor() {
        UserDto userDto = new UserDto(1, "Маша", "masha@example.com");

        assertEquals(1, userDto.getId());
        assertEquals("Маша", userDto.getName());
        assertEquals("masha@example.com", userDto.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        UserDto userDto1 = new UserDto(1, "Маша", "masha@example.com");
        UserDto userDto2 = new UserDto(1, "Маша2", "masha2@example.com");
        UserDto userDto3 = new UserDto(2, "Даша", "dasha@example.com");

        assertEquals(userDto1, userDto2);
        assertNotEquals(userDto1.hashCode(), userDto3.hashCode());
    }

    @Test
    public void testToString() {
        UserDto userDto = new UserDto(1, "Маша", "masha@example.com");
        String expectedToString = "UserDto(id=1, name=Маша, email=masha@example.com)";

        assertEquals(expectedToString, userDto.toString());
    }
}