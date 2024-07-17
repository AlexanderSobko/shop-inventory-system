package ru.kitayanochka.shop_inventory_system.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterDto {

    @Positive(message = "Id must be a positive number!")
    Integer id;
    String firstName;
    String lastName;
    @Email
    String email;
    String city;
    String phone;
    @Min(value = 0, message = "From can't be negative!")
    int from = 0;
    @Positive(message = "Size must be a positive number!")
    int size = 10;

}
