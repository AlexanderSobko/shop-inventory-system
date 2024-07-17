package ru.kitayanochka.shop_inventory_system.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.kitayanochka.shop_inventory_system.user.dto.UserFilterDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(scripts = "/sql/initialUserData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/deleteUserData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserServiceImpFilterTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/filtrationUserTestData.csv", delimiter = '|')
    void getUsers(String filter, int resultCount) throws JsonProcessingException {
        UserFilterDto dto = objectMapper.readValue(filter, UserFilterDto.class);
        List<User> users = userService.getUsers(dto);

        assertEquals(resultCount, users.size());
    }

}
