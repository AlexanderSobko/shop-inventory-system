package ru.kitayanochka.shop_inventory_system.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserFilterDto;
import ru.kitayanochka.shop_inventory_system.user.service.UserService;

import java.util.List;

import static ru.kitayanochka.shop_inventory_system.user.mapper.UserMapper.USER_MAPPER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/admin")
public class UserPrivateController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreationDto dto) {
        return ResponseEntity.status(201).body(USER_MAPPER.mapToUserDto(service.createUser(dto)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Positive int id,
                                              @Valid @RequestBody UserCreationDto dto) {
        return ResponseEntity.ok(USER_MAPPER.mapToUserDto(service.updateUser(id, dto)));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@Valid @RequestBody UserFilterDto userFilterDto) {
        return ResponseEntity.ok(USER_MAPPER.mapToUserDto(service.getUsers(userFilterDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive int id) {
        service.deleteUser(id);
    }

}
