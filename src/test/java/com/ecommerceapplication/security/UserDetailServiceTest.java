package com.ecommerceapplication.security;

import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    void testLoadUserByUsername1() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        String password = "password";
        user.setPassword(password);
        user.setUserId(0L);
        when(userRepository.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        assertNotNull(authorityCollection);
        assertEquals(0, authorityCollection.size());
        assertEquals(password, userDetails.getPassword());
        assertEquals(username, userDetails.getUsername());
    }
}
