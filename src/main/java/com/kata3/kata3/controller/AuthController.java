package com.kata3.kata3.controller;

import com.kata3.kata3.data.dto.UserDto;
import com.kata3.kata3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        String token = userService.register(userDto);
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        String token = userService.login(userDto);
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.badRequest().build();
    }
}
