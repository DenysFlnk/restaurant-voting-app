package com.restaurantvoting.controller;

import com.restaurantvoting.entity.User;
import com.restaurantvoting.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);


    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll(){
        log.info("getAll");
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id){
        log.info("get {}", id);
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody @Valid User user){
        log.info("create {}", user);
        checkNew(user);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("delete {}", id);
        User deleteUser = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        userRepository.delete(deleteUser);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid User user, @PathVariable int id){
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userRepository.save(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }
}
