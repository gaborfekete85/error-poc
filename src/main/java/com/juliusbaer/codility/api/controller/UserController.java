package com.juliusbaer.codility.api.controller;

import com.juliusbaer.codility.api.domain.ApiUser;
import com.juliusbaer.codility.error.ErrorCodes;
import com.juliusbaer.codility.error.exceptions.UnexpectedBusinessException;
import com.juliusbaer.codility.error.exceptions.UserNotFoundException;
import com.juliusbaer.codility.repository.UserRepository;
import com.juliusbaer.codility.repository.entity.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(("/api"))
public class UserController {

    public final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public UserController(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity helloWorld(@RequestParam(required = false, defaultValue = "Anonymus") final String name) {
        return ResponseEntity.ok(String.format("Hello %s.", name));
    }

    @PostMapping("/user")
    public ResponseEntity<ApiUser> createUser(@RequestBody ApiUser apiUser) {
        if(userRepository.existsByEmail(apiUser.getEmail())) {
            throw new UnexpectedBusinessException(ErrorCodes.PCO_1004, Map.of("EMAIL", apiUser.getEmail()));
        }
        LOGGER.info("Creating user {}", apiUser);
        User user = modelMapper.map(apiUser, User.class);
        User createdUser = userRepository.save(user);
        return ResponseEntity.ok(modelMapper.map(createdUser, ApiUser.class));
    }

    @PutMapping("/user")
    public ResponseEntity<ApiUser> updateUser(@RequestBody ApiUser apiUser) {
        LOGGER.info("Updating user {}", apiUser);
        User user = modelMapper.map(apiUser, User.class);
        Optional<User> optionalUser = userRepository.findById(apiUser.getUserId());
        if(optionalUser.isEmpty()) {
            throw new UnexpectedBusinessException(ErrorCodes.PCO_1001, Map.of("USER_ID", user.getId()));
        }

        User userToUpdate = optionalUser.get();
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setName(user.getName());
        User createdUser = userRepository.save(userToUpdate);
        return ResponseEntity.ok(modelMapper.map(createdUser, ApiUser.class));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ApiUser>> getUsers() {
        LOGGER.info("Query All Users");
        List<User> users = userRepository.findAll();
        List<ApiUser> response = users.stream().map(user -> modelMapper.map(user, ApiUser.class)).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiUser> getUserById(@PathVariable UUID userId) throws UserNotFoundException {
        LOGGER.info("Query user By userId: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), ApiUser.class));
        }
        throw new UserNotFoundException(userId);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<UUID> deleteUser(@PathVariable UUID userId) {
        LOGGER.info("Delete user with id {]", userId);
        userRepository.deleteById(userId);
        return ResponseEntity.ok(userId);
    }
}