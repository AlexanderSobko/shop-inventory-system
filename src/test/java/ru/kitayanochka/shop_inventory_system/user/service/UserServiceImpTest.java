package ru.kitayanochka.shop_inventory_system.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kitayanochka.shop_inventory_system.exception.AlreadyExistsException;
import ru.kitayanochka.shop_inventory_system.exception.NotFoundException;
import ru.kitayanochka.shop_inventory_system.user.UserRepository;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.kitayanochka.shop_inventory_system.exception.message.UserExceptionMessage.*;
import static ru.kitayanochka.shop_inventory_system.user.mapper.UserMapper.USER_MAPPER;

@SpringBootTest
class UserServiceImpTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImp userService;

    private final EasyRandom easyRandom = new EasyRandom();

    @Autowired
    EntityManager entityManager;

    @Test
    void createUser() {
        UserCreationDto creationDto = easyRandom.nextObject(UserCreationDto.class);
        User user = USER_MAPPER.mapToUser(creationDto);
        user.setId(1);
        when(userRepo.existsByEmail(Mockito.any())).thenReturn(false);
        when(userRepo.existsByPhone(Mockito.any())).thenReturn(false);
        when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        User newUser = userService.createUser(creationDto);

        assertNotNull(newUser);
        assertEquals(creationDto.getFirstName(), newUser.getFirstName());
        assertEquals(creationDto.getLastName(), newUser.getLastName());
        assertEquals(creationDto.getEmail(), newUser.getEmail());
        assertEquals(creationDto.getPhone(), newUser.getPhone());
        assertEquals(creationDto.getCity(), newUser.getCity());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void failCreateUserEmailExists() {
        UserCreationDto creationDto = easyRandom.nextObject(UserCreationDto.class);
        when(userRepo.existsByEmail(Mockito.any())).thenReturn(true);
        when(userRepo.existsByPhone(Mockito.any())).thenReturn(false);

        AlreadyExistsException alreadyExistsException = assertThrows(AlreadyExistsException.class, () -> {
            userService.createUser(creationDto);
        });

        assertEquals(alreadyExistsException.getMessage(), EMAIL_ALREADY_IN_USE.getMessage(creationDto.getEmail()));

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void failCreateUserPhoneExists() {
        UserCreationDto creationDto = easyRandom.nextObject(UserCreationDto.class);
        when(userRepo.existsByEmail(Mockito.any())).thenReturn(false);
        when(userRepo.existsByPhone(Mockito.any())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            userService.createUser(creationDto);
        });

        assertEquals(exception.getMessage(), PHONE_ALREADY_IN_USE.getMessage(creationDto.getPhone()));

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void getUser() {
        User user = easyRandom.nextObject(User.class);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void failGetUser() {
        int userId = 1;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.getUser(userId);
        });

        assertEquals(exception.getMessage(), USER_NOT_FOUND.getMessage(userId));
    }

    @Test
    void updateUser() {
        int userId = 1;
        UserCreationDto dto = easyRandom.nextObject(UserCreationDto.class);

        User existingUser = USER_MAPPER.mapToUser(dto);
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setPhone("+72345678900");

        when(userRepo.existsByPhone(dto.getPhone())).thenReturn(false);
        when(userRepo.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any(User.class))).thenReturn(USER_MAPPER.updateUser(existingUser, dto));

        User updatedUser = userService.updateUser(userId, dto);

        assertNotNull(updatedUser);
        assertEquals(dto.getEmail(), updatedUser.getEmail());
        assertEquals(dto.getPhone(), updatedUser.getPhone());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void failUpdateUserEmailExists() {
        int userId = 1;
        UserCreationDto dto = easyRandom.nextObject(UserCreationDto.class);

        User existingUser = USER_MAPPER.mapToUser(dto);
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setPhone("+72345678900");

        when(userRepo.existsByPhone(dto.getPhone())).thenReturn(false);
        when(userRepo.existsByEmail(dto.getEmail())).thenReturn(true);
        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            userService.updateUser(userId, dto);
        });

        assertEquals(exception.getMessage(), EMAIL_ALREADY_IN_USE.getMessage(dto.getEmail()));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void failUpdateUserPhoneExists() {
        int userId = 1;
        UserCreationDto dto = easyRandom.nextObject(UserCreationDto.class);

        User existingUser = USER_MAPPER.mapToUser(dto);
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setPhone("+72345678900");

        when(userRepo.existsByPhone(dto.getPhone())).thenReturn(true);
        when(userRepo.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            userService.updateUser(userId, dto);
        });

        assertEquals(exception.getMessage(), PHONE_ALREADY_IN_USE.getMessage(dto.getPhone()));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void failUpdateUserUserNotFound() {
        int userId = 1;
        UserCreationDto dto = easyRandom.nextObject(UserCreationDto.class);

        User existingUser = USER_MAPPER.mapToUser(dto);
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setPhone("+72345678900");

        when(userRepo.existsByPhone(dto.getPhone())).thenReturn(false);
        when(userRepo.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.updateUser(userId, dto);
        });

        assertEquals(exception.getMessage(), USER_NOT_FOUND.getMessage(userId));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void deleteUser() {
        int userId = 1;
        User user = easyRandom.nextObject(User.class);
        user.setId(userId);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepo, times(1)).delete(user);
    }

    @Test
    void failDeleteUserNotFound() {
        int userId = 1;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals(exception.getMessage(), USER_NOT_FOUND.getMessage(userId));
        verify(userRepo, never()).delete(any(User.class));
    }


}