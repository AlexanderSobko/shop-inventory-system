package ru.kitayanochka.shop_inventory_system.user.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationDto {

    @NotBlank(message = "Firstname can't be blank!")
    @Size(min = 2, max = 20, message = "Firstname must be between 2 and 20 characters!")
    String firstName;
    @NotBlank(message = "Lastname can't be blank!")
    @Size(min = 2, max = 20, message = "Lastname must be between 2 and 20 characters!")
    String lastName;
    @Email
    @NotBlank(message = "Email can't be blank!")
    String email;
    @NotBlank(message = "The phone number can't be blank!")
    @Pattern(regexp = "\\+(\\d){11}", message = "The phone number must be valid!")
    String phone;
    @NotBlank(message = "City can't be blank!")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters!")
    String city;

}