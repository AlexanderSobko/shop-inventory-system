package ru.kitayanochka.shop_inventory_system.user.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    User mapToUser(UserCreationDto dto);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUser(@MappingTarget User user, UserCreationDto dto);


}
