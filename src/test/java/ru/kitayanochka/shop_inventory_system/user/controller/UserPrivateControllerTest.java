package ru.kitayanochka.shop_inventory_system.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserFilterDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;
import ru.kitayanochka.shop_inventory_system.user.service.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserPrivateControllerTest {

    private final String basePath = "http://localhost:8080/api/v1/users/admin";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/invalidUserFilterData.csv", delimiter = '|')
    void failGetUsersInvalidFilter(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(get(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }

    @Test
    void getUsers() throws Exception {
        UserFilterDto filter = new UserFilterDto();
        mockMvc.perform(get(basePath)
                        .content(objectMapper.writeValueAsString(filter))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/validUsers.csv", delimiter = '|')
    public void saveValidUser(String fileJson, String expectedResponse) throws Exception {
        User expectedUser = objectMapper.readValue(expectedResponse, User.class);
        when(userService.createUser(Mockito.any(UserCreationDto.class)))
                .thenReturn(expectedUser);
        mockMvc.perform(post(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/invalidUsers.csv", delimiter = '|')
    public void failSaveInvalidUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(post(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/validUsers.csv", delimiter = '|')
    void updateValidUser(String fileJson, String expectedResponse) throws Exception {
        User expectedUser = objectMapper.readValue(expectedResponse, User.class);
        when(userService.updateUser(Mockito.anyInt(), Mockito.any(UserCreationDto.class)))
                .thenReturn(expectedUser);
        mockMvc.perform(patch(basePath + "/1")
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controller/user-test-data/invalidUsers.csv", delimiter = '|')
    void failUpdateInvalidUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(patch(basePath + "/1")
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    void deleteUser() throws Exception {
        int userId = 1;
        doNothing().when(userService).deleteUser(userId);
        mockMvc.perform(delete(basePath + "/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}