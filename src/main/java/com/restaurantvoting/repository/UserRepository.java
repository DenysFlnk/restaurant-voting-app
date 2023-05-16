package com.restaurantvoting.repository;

import com.restaurantvoting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserByEmailIgnoreCase(String email);
}
