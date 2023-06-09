package com.restaurantvoting.controller;

import com.restaurantvoting.AbstractControllerTest;
import com.restaurantvoting.JsonUtil;
import com.restaurantvoting.TestUtil;
import com.restaurantvoting.entity.User;
import com.restaurantvoting.error.NotFoundException;
import com.restaurantvoting.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.restaurantvoting.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URI)
                        .with(TestUtil.userHttpBasic(admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<User> actual = JsonUtil.readValuesFromJson(getContentAsString(result), User.class);

        assertIterableEquals(allUsers, actual);
    }

    @Test
    void getAllUnauthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URI))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URI + "/" + user1.id())
                        .with(TestUtil.userHttpBasic(admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        User actual = JsonUtil.readValueFromJson(getContentAsString(result), User.class);

        assertEquals(user1, actual);
    }

    @Test
    void create() throws Exception {
        User newUser = getNew();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URI)
                        .with(TestUtil.userHttpBasic(admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        User created = JsonUtil.readValueFromJson(getContentAsString(result), User.class);
        newUser.setId(created.id());

        assertEquals(newUser, created);
        assertEquals(newUser, userRepository.findById(newUser.id()).orElse(null));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URI + "/" + user1.id())
                        .with(TestUtil.userHttpBasic(admin1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> userRepository.findById(user1.id()).orElseThrow(() -> new NotFoundException("")));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URI + "/" + TestUtil.ID_NOT_FOUND)
                        .with(TestUtil.userHttpBasic(admin1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        User updateUser = getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URI + "/" + admin1.id())
                        .with(TestUtil.userHttpBasic(admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(updateUser, "gqfqf123")))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(updateUser, userRepository.findById(updateUser.id()).orElse(null));
    }

    @Test
    void enable() throws Exception {
        User disabled = getDisabled();

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URI + "/" + user1.id() + "?enabled=false")
                        .with(TestUtil.userHttpBasic(admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(disabled, "12345")))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(disabled, userRepository.findById(user1.id()).orElse(null));
    }
}