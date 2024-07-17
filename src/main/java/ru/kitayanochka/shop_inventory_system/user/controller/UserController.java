package ru.kitayanochka.shop_inventory_system.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserDto;
import ru.kitayanochka.shop_inventory_system.user.service.UserService;

import static ru.kitayanochka.shop_inventory_system.user.mapper.UserMapper.USER_MAPPER;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable @Positive int id) {
        return ResponseEntity.ok(USER_MAPPER.mapToUserDto(service.getUser(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Positive int id,
                                              @Valid @RequestBody UserCreationDto dto) {
        return ResponseEntity.ok(USER_MAPPER.mapToUserDto(service.updateUser(id, dto)));
    }

}
