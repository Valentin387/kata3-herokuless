package com.kata3.kata3.service;

import com.kata3.kata3.data.dto.UserDto;
import com.kata3.kata3.data.entity.User;
import com.kata3.kata3.data.repository.UserRepository;
import com.kata3.kata3.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        user = new User();
        user.setId("12345");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
    }

    @Test
    void register_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken("12345", "testuser")).thenReturn("jwtToken");

        String token = userService.register(userDto);

        assertEquals("jwtToken", token);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken("12345", "testuser");
    }

    @Test
    void register_duplicateUsername_returnsNull() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String token = userService.register(userDto);

        assertNull(token);
        verify(userRepository).findByUsername("testuser");
        verifyNoInteractions(passwordEncoder, jwtUtil);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("12345", "testuser")).thenReturn("jwtToken");

        String token = userService.login(userDto);

        assertEquals("jwtToken", token);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken("12345", "testuser");
    }

    @Test
    void login_invalidUsername_returnsNull() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        String token = userService.login(userDto);

        assertNull(token);
        verify(userRepository).findByUsername("testuser");
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void login_incorrectPassword_returnsNull() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        String token = userService.login(userDto);

        assertNull(token);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verifyNoInteractions(jwtUtil);
    }
}