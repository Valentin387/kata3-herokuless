package com.kata3.kata3.data.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String token;

    public TokenDto(String token) {
        this.token = token;
    }
}