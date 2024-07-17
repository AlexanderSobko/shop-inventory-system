package ru.kitayanochka.shop_inventory_system.user.service;

import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserFilterDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserCreationDto dto);

    User updateUser(int id, UserCreationDto dto);

    User getUser(int id);

    List<User> getUsers(UserFilterDto filter);

    void deleteUser(int id);

}
