package com.restaurantvoting.security;

import com.restaurantvoting.entity.User;
import jakarta.validation.constraints.NotNull;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    @NotNull
    private final User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int id() {
        return user.id();
    }
}
