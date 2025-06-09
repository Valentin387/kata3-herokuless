package com.kata3.kata3.service;


import com.kata3.kata3.data.dto.UserDto;
import com.kata3.kata3.data.entity.User;
import com.kata3.kata3.data.repository.UserRepository;
import com.kata3.kata3.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return null;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return jwtUtil.generateToken(savedUser.getId(), userDto.getUsername());
    }

    public String login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getId(), userDto.getUsername());
        }
        return null;
    }
}
