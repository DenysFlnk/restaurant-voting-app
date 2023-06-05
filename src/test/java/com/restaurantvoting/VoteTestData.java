package com.restaurantvoting;

import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.to.VoteTo;
import com.restaurantvoting.util.VotesUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VoteTestData {
    public static final String VOTE_CONTROLLER_RESTAURANTS_URI = "/user/restaurants";

    public static final String VOTE_CONTROLLER_VOTES_URI = "/user/votes";

    public static final String RESULTS_URI = "/results";

    public static final LocalDateTime INVALID_VOTE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 32, 0));

    public static final LocalDateTime VALID_VOTE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 17, 28));

    public static final Vote vote1 = new Vote(1, 1, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30, 0)));

    public static final Vote vote2 = new Vote(2, 2, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0, 0)));

    public static final Vote vote3 = new Vote(3, 3, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 54, 0)));

    public static final List<Vote> allCurrentDayVotes = List.of(vote1, vote2, vote3);

    public static final List<VoteTo> allVoteTosForUser1 = VotesUtil.getTos(List.of(vote1));

    public static Vote getNew() {
        return new Vote(null, null, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 55, 0)));
    }

    public static Vote getUpdated() {
        Vote updated = new Vote(vote2);

        updated.setRestaurantId(3);

        return updated;
    }
}
