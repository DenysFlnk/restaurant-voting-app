package com.restaurantvoting.repository;

import com.restaurantvoting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> getAllByRestaurantIdAndDateTimeBetween(int restaurantId, LocalDateTime start, LocalDateTime end);

    Vote getVoteByUserIdAndDateTimeBetween(int userId, LocalDateTime start, LocalDateTime end);
}
