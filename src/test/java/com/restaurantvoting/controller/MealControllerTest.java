package com.restaurantvoting.controller;

import com.restaurantvoting.*;
import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.error.NotFoundException;
import com.restaurantvoting.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.restaurantvoting.MealTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MealControllerTest extends AbstractControllerTest {

    @Autowired
    private MealRepository mealRepository;

    @Test
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getMealControllerUri(RestaurantTestData.restaurant.id()))
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Meal> actual = JsonUtil.readValuesFromJson(getContentAsString(result), Meal.class);

        assertIterableEquals(allRestaurantMeals, actual);
    }

    @Test
    void get() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getMealControllerUri(RestaurantTestData.restaurant.id())
                                + "/" + steak.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Meal actual = JsonUtil.readValueFromJson(getContentAsString(result), Meal.class);

        assertEquals(steak, actual);
    }

    @Test
    void getForeignMeal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(getMealControllerUri(RestaurantTestData.restaurant.id())
                                + "/" + salad.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Meal newMeal = getNew();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(getMealControllerUri(RestaurantTestData.restaurant.id()))
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Meal created = JsonUtil.readValueFromJson(getContentAsString(result), Meal.class);
        newMeal.setId(created.id());

        assertEquals(newMeal, created);
        assertEquals(newMeal, mealRepository.findById(newMeal.id()).orElse(null));
    }

    @Test
    void createNotNew() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(getMealControllerUri(RestaurantTestData.restaurant.id()))
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(steak)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(getMealControllerUri(RestaurantTestData.restaurant.id()) + "/" + steak.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> mealRepository.findById(steak.id()).orElseThrow(() -> new NotFoundException("")));
    }

    @Test
    void update() throws Exception {
        Meal updateMeal = getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(getMealControllerUri(RestaurantTestData.bistro.id())
                                + "/" + salad.id())
                        .with(TestUtil.userHttpBasic(UserTestData.admin1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updateMeal)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(updateMeal, mealRepository.findById(updateMeal.id()).orElse(null));
    }
}