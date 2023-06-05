package com.restaurantvoting.controller;

import com.restaurantvoting.*;
import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.error.NotFoundException;
import com.restaurantvoting.repository.VoteRepository;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.to.RestaurantVoteSummaryTo;
import com.restaurantvoting.to.VoteTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.restaurantvoting.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getAllActiveRestaurants() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(VOTE_CONTROLLER_RESTAURANTS_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<RestaurantTo> actual = JsonUtil.readValuesFromJson(getContentAsString(result), RestaurantTo.class);

        assertIterableEquals(RestaurantTestData.activeRestaurantTos, actual);
    }

    @Test
    void vote() throws Exception {
        Vote newVote = getNew();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(VOTE_CONTROLLER_RESTAURANTS_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newVote)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Vote created = JsonUtil.readValueFromJson(getContentAsString(result), Vote.class);
        assertEquals(vote1.id(), created.id());

        newVote.setUserId(created.getUserId());
        newVote.setId(created.getId());

        assertEquals(newVote, created);
        assertEquals(newVote, voteRepository.findById(newVote.id()).orElse(null));
    }

    @Test
    void voteNotNew() throws Exception {
        Vote newVote = getNew();
        newVote.setId(vote1.getId());

        mockMvc.perform(MockMvcRequestBuilders.post(VOTE_CONTROLLER_RESTAURANTS_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newVote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getResultOfVoting() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(VOTE_CONTROLLER_VOTES_URI + RESULTS_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<RestaurantVoteSummaryTo> actual = JsonUtil.readValuesFromJson(getContentAsString(result), RestaurantVoteSummaryTo.class);

        assertIterableEquals(RestaurantTestData.restaurantVoteSummaryTos, actual);
    }

    @Test
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(VOTE_CONTROLLER_VOTES_URI)
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<VoteTo> actual = JsonUtil.readValuesFromJson(getContentAsString(result), VoteTo.class);

        assertIterableEquals(allVoteTosForUser1, actual);
    }

    @Test
    void get() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(VOTE_CONTROLLER_VOTES_URI + "/" + vote1.id())
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Vote actual = JsonUtil.readValueFromJson(getContentAsString(result), Vote.class);

        assertEquals(vote1, actual);
    }

    @Test
    void getForeignVote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_CONTROLLER_VOTES_URI + "/" + vote2.id())
                        .with(TestUtil.userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        Vote updateVote = getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_CONTROLLER_VOTES_URI + "/" + vote2.id() +
                                "?dateTime=" + VALID_VOTE_TIME)
                        .with(TestUtil.userHttpBasic(UserTestData.user2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updateVote)))
                .andDo(print())
                .andExpect(status().isNoContent());

        updateVote.setDateTime(VALID_VOTE_TIME);

        assertEquals(updateVote, voteRepository.findById(updateVote.id()).orElse(null));
    }

    @Test
    void updateInvalidTime() throws Exception {
        Vote updateVote = getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_CONTROLLER_VOTES_URI + "/" + vote2.id() +
                                "?dateTime=" + INVALID_VOTE_TIME)
                        .with(TestUtil.userHttpBasic(UserTestData.user2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updateVote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(VOTE_CONTROLLER_VOTES_URI + "/" + vote2.id() +
                                "?dateTime=" + VALID_VOTE_TIME)
                        .with(TestUtil.userHttpBasic(UserTestData.user2)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> voteRepository.findById(vote2.id()).orElseThrow(() -> new NotFoundException("")));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(VOTE_CONTROLLER_VOTES_URI + "/" + TestUtil.ID_NOT_FOUND +
                                "?dateTime=" + VALID_VOTE_TIME)
                        .with(TestUtil.userHttpBasic(UserTestData.user2)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}