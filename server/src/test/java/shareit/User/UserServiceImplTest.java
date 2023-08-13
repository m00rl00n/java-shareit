package shareit.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class    UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void getUserByIdTest() {
        User user = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto result = userService.getUser(user.getId());

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(userRepository).findById(1);
    }

    @Test
    public void getUserByWrongIdTest() {
        User user = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(user.getId()));

        verify(userRepository).findById(user.getId());
    }


    @Test
    public void updateUserTest() {
        User existingUser = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(existingUser);

        UserDto updatedUserDto = createUserDto("Даша", "dasha@yandex.ru");
        UserDto result = userService.update(updatedUserDto, existingUser.getId());

        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());

        verify(userRepository).findById(existingUser.getId());
        verify(userRepository).save(any(User.class));

        User savedUser = userCaptor.getValue();
        assertEquals(existingUser.getId(), savedUser.getId());
        assertEquals(updatedUserDto.getName(), savedUser.getName());
        assertEquals(updatedUserDto.getEmail(), savedUser.getEmail());
    }

    @Test
    public void updateWrongUserTest() {
        User user = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserDto updatedUserDto = createUserDto("Даша", "dasha@yandex.ru");
        assertThrows(NotFoundException.class, () -> userService.update(updatedUserDto, user.getId()));

        verify(userRepository).findById(user.getId());
    }

    @Test
    public void deleteUserTest() {
        User user = createUser(1, "Маша", "masha@yandex.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.remove(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    private User createUser(Integer id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private UserDto createUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userDto;
    }
}





