package com.restaurantvoting.controller;

import com.restaurantvoting.AbstractControllerTest;
import com.restaurantvoting.JsonUtil;
import com.restaurantvoting.TestUtil;
import com.restaurantvoting.UserTestData;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.error.ErrorInfo;
import com.restaurantvoting.error.ErrorType;
import com.restaurantvoting.error.NotFoundException;
import com.restaurantvoting.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.restaurantvoting.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_CONTROLLER_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Restaurant> actual = JsonUtil.readValuesFromJson(getContentAsString(result), Restaurant.class);

        assertIterableEquals(allRestaurants, actual);
    }

    @Test
    void getAllUnauthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_CONTROLLER_URI))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllAccessDenied() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_CONTROLLER_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ErrorInfo error = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorType.FORBIDDEN, error.type());
        assertEquals("Access Denied", error.details()[0]);
    }

    @Test
    void get() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_CONTROLLER_URI + "/" + cafeteria.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Restaurant actual = JsonUtil.readValueFromJson(getContentAsString(result), Restaurant.class);

        assertEquals(cafeteria, actual);
    }

    @Test
    void getNotFound() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_CONTROLLER_URI + "/" + TestUtil.ID_NOT_FOUND)
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ErrorInfo error = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorType.NOT_FOUND, error.type());

        String expectedDetail = String.format("Not found entity with id=%s", TestUtil.ID_NOT_FOUND);
        assertEquals(expectedDetail, error.details()[0]);
    }

    @Test
    void create() throws Exception {
        Restaurant newRest = getNew();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_CONTROLLER_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newRest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Restaurant created = JsonUtil.readValueFromJson(getContentAsString(result), Restaurant.class);

        newRest.setId(created.id());

        assertEquals(newRest, created);
        assertEquals(newRest, restaurantRepository.findById(newRest.id()).orElse(null));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESTAURANT_CONTROLLER_URI + "/" + cafeteria.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> restaurantRepository.findById(cafeteria.id()).orElseThrow(() -> new NotFoundException("")));
    }

    @Test
    void update() throws Exception {
        Restaurant updateRest = getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(RESTAURANT_CONTROLLER_URI + "/" + cafeteria.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updateRest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(updateRest, restaurantRepository.findById(updateRest.id()).orElse(null));
    }

    @Test
    void updateInconsistent() throws Exception {
        Restaurant updateRest = getUpdated();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RESTAURANT_CONTROLLER_URI + "/" + restaurant.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updateRest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ErrorInfo error = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorType.BAD_REQUEST, error.type());

        String expectedDetail = String.format("Restaurant must has id=%s", restaurant.id());
        assertEquals(expectedDetail, error.details()[0]);
    }

    @Test
    void activate() throws Exception {
        Restaurant deactivated = getDeactivated();

        mockMvc.perform(MockMvcRequestBuilders.patch(RESTAURANT_CONTROLLER_URI + "/" + bistro.id() + "?active=false")
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(deactivated, restaurantRepository.findById(deactivated.id()).orElse(null));
    }
}