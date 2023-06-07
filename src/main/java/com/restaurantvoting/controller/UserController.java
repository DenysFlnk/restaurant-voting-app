package com.restaurantvoting.controller;

import com.restaurantvoting.entity.User;
import com.restaurantvoting.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.util.List;

@Tag(name = "Users", description = "User accounts management API`s")
@RestController
@RequestMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);


    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Retrieve all users")
    @GetMapping
    public List<User> getAll(){
        log.info("getAll");
        return userRepository.findAll();
    }

    @Operation(summary = "Retrieve user by Id")
    @GetMapping("/{id}")
    public User get(@PathVariable int id){
        log.info("get {}", id);
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    @Operation(summary = "Create new user")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid User user){
        log.info("create {}", user);
        checkNew(user);
        return userRepository.save(user);
    }

    @Operation(summary = "Delete user by Id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("delete {}", id);
        User deleteUser = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        userRepository.delete(deleteUser);
    }

    @Operation(summary = "Modify user")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid User user, @PathVariable int id){
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userRepository.save(user);
    }

    @Operation(summary = "Enable or disable user by Id")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }
}
