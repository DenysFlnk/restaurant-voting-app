package com.restaurantvoting.util;

import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.to.VoteTo;

import java.time.LocalDateTime;
import java.util.List;

import static com.restaurantvoting.util.DateTimeUtil.*;
public class VotesUtil {

    private VotesUtil() {
    }

    public static List<VoteTo> getTos(List<Vote> votes){
        LocalDateTime currentDayTime = LocalDateTime.now();
        return votes.stream()
                .map(vote -> createTo(vote, currentDayTime.isAfter(startOfCurrentDay)
                        && currentDayTime.isBefore(endOfVoting)))
                .toList();
    }

    public static VoteTo createTo(Vote vote, boolean editable){
        return new VoteTo(vote.getId(), vote.getUserId(), vote.getRestaurantId(), vote.getDateTime(), editable);
    }

}
