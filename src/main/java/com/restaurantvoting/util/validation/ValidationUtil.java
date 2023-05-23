package com.restaurantvoting.util.validation;

import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.error.IllegalRequestDataException;
import com.restaurantvoting.error.NotFoundException;
import org.springframework.data.domain.Persistable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.restaurantvoting.util.DateTimeUtil.*;

public final class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        if (object == null) {
            throw new NotFoundException("Not found entity with id=" + id);
        }
        return object;
    }

    public static void checkNew(Persistable<Integer> bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(Persistable<Integer> bean, int id) {
        Assert.notNull(bean.getId(), "Entity must has id");
        if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void assureVotePermission(Vote vote) {
        if (vote.getDateTime().isBefore(startOfCurrentDay) || vote.getDateTime().isAfter(endOfVoting)) {
            throw new IllegalRequestDataException(String.format("Vote dateTime must be in range %s - %s",
                    startOfCurrentDay.format(DATE_TIME_FORMATTER), endOfVoting.format(DATE_TIME_FORMATTER)));
        }
    }

    public static void assureChangeVotePermission(Vote vote, LocalDateTime changeDateTime) {
        LocalDateTime endOfParticularVoting = LocalDateTime.of(vote.getDateTime().toLocalDate(), LocalTime.of(11, 0, 0));
        if (changeDateTime.isAfter(endOfParticularVoting)) {
            throw new IllegalRequestDataException("It`s too late to change your vote, dateTime must be before end of voting - " +
                    endOfParticularVoting.format(DATE_TIME_FORMATTER));
        }
    }

    public static void checkVoteBelongsUser(Vote vote, int userId) {
        if (vote.id() != userId) {
            throw new AccessDeniedException("You do not have permission to see/modify other user votes");
        }
    }
}