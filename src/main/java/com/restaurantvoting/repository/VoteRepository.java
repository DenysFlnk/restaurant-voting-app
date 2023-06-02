package com.restaurantvoting.repository;

import com.restaurantvoting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote getVoteByUserIdAndDateTimeBetween(int userId, LocalDateTime start, LocalDateTime end);

    List<Vote> getAllByUserId(int userId);

    Optional<Vote> getByIdAndUserId(int voteId, int userId);

    List<Vote> getAllByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
