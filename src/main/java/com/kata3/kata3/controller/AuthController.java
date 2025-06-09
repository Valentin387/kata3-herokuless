package com.kata3.kata3.controller;

import com.kata3.kata3.data.dto.TokenDto;
import com.kata3.kata3.data.dto.UserDto;
import com.kata3.kata3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> register(@RequestBody UserDto userDto) {
        String token = userService.register(userDto);
        return token != null ? ResponseEntity.ok(new TokenDto(token)) : ResponseEntity.badRequest().build();
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> login(@RequestBody UserDto userDto) {
        String token = userService.login(userDto);
        return token != null ? ResponseEntity.ok(new TokenDto(token)) : ResponseEntity.badRequest().build();
    }
}
