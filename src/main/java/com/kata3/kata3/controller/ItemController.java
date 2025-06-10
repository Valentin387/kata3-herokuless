package com.kata3.kata3.controller;

import com.kata3.kata3.data.dto.ItemDto;
import com.kata3.kata3.data.entity.Item;
import com.kata3.kata3.service.ItemService;
import com.kata3.kata3.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private JwtUtil jwtUtil;

    private String getUserIdFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new IllegalStateException("No valid JWT token found in Authorization header");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getItems(@RequestParam(required = false) Item.ItemType type, HttpServletRequest request) {
        String userId = getUserIdFromToken(request);
        List<ItemDto> items = itemService.getItemsByUser(userId, type);
        return ResponseEntity.ok(items);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> getItem(@PathVariable String id, HttpServletRequest request) {
        String userId = getUserIdFromToken(request);
        ItemDto item = itemService.getItemById(id, userId);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto, HttpServletRequest request) {
        String userId = getUserIdFromToken(request);
        ItemDto createdItem = itemService.createItem(itemDto, userId);
        return ResponseEntity.ok(createdItem);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> updateItem(@PathVariable String id, @RequestBody ItemDto itemDto, HttpServletRequest request) {
        String userId = getUserIdFromToken(request);
        ItemDto updatedItem = itemService.updateItem(id, itemDto, userId);
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> deleteItem(@PathVariable String id, HttpServletRequest request) {
        String userId = getUserIdFromToken(request);
        boolean deleted = itemService.deleteItem(id, userId);
        return deleted ? ResponseEntity.ok(Map.of("success", true)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> resetItems() {
        itemService.deleteAll();
        return ResponseEntity.ok(Map.of("success", true));
    }
}