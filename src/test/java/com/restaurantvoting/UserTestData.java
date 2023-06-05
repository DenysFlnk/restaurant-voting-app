package com.restaurantvoting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurantvoting.entity.Role;
import com.restaurantvoting.entity.User;

import java.util.List;
import java.util.Set;

public class UserTestData {
    public static final String USER_CONTROLLER_URI = "/admin/users";

    public static final User user1 = new User(1, "user1", "user1@gmail.com", "12345", true, Set.of(Role.USER));

    public static final User user2 = new User(2, "user2", "user2@gmail.com", "abcd", true, Set.of(Role.USER));

    public static final User admin1 = new User(3, "admin1", "admin1@gmail.com", "gqfqf123", true, Set.of(Role.USER, Role.ADMIN));

    public static final List<User> allUsers = List.of(user1, user2, admin1);

    public static User getNew() {
        return new User(null, "user3", "user3@gmail.com", "krbyu", true, Set.of(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(admin1);

        updated.setName("Best admin1");

        return updated;
    }

    public static User getDisabled() {
        User disabled = new User(user1);

        disabled.setEnabled(false);

        return disabled;
    }

    public static String jsonWithPassword(User user, String pass) throws JsonProcessingException {
        return JsonUtil.writeAdditionProps(user, "password", pass);
    }
}
