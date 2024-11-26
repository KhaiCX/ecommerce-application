package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Cart;
import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.repository.CartRepository;
import com.ecommerceapplication.repository.UserRepository;
import com.ecommerceapplication.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }
    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        logger.info("Start finding user by username '{}'", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("Invalid username. Failed to retrieve user '{}'", username);
            return ResponseEntity.notFound().build();
        }

        logger.info("Successfully retrieved user '{}'", username);
        logger.info("End finding user by username '{}'", username);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        logger.info("Start creating user '{}'", createUserRequest.getUsername());

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);

        String password = createUserRequest.getPassword();
        if (password == null || password.length() < 7 || !password.equals(createUserRequest.getConfirmPassword())) {
            logger.error("Invalid password. Failed to create user '{}'", user.getUsername());
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);

        logger.info("User '{}' was successfully created", user.getUsername());
        logger.info("End creating user '{}'", createUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }
}
