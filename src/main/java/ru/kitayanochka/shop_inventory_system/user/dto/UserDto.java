package ru.kitayanochka.shop_inventory_system.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    int id;
    String firstName;
    String lastName;
    String email;
    String city;
    String phone;

}
