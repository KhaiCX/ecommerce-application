package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.repository.CartRepository;
import com.ecommerceapplication.repository.UserRepository;
import com.ecommerceapplication.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    @Test
    public void findUserById() {
        long id = 1L;
        User mockUser = new User();
        mockUser.setUserId(id);
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.findById(id);
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(user.getUserId(), id);
    }
    @Test
    public void findUserByUsername() {
        User mockUser = new User();
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.findByUserName(TEST_USERNAME);
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(user.getUsername(), TEST_USERNAME);
    }
    @Test
    public void findUserByNonExistentUsername() {
        User mockUser = new User();
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.findByUserName("");
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }
    @Test
    public void creationRequestHasValidCredentials() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(TEST_PASSWORD);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);
        createUserRequest.setPassword(TEST_PASSWORD);
        createUserRequest.setConfirmPassword(TEST_PASSWORD);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User createdUser = response.getBody();

        assertNotNull(createdUser);
        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(createdUser.getUsername(), TEST_USERNAME);
        assertEquals(createdUser.getPassword(), TEST_PASSWORD);
    }
    @Test
    public void creationRequestHasInvalidPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);
        createUserRequest.setPassword(TEST_PASSWORD);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
    }
    @Test
    public void creationRequestIsWithoutMatchingPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
    }
}
