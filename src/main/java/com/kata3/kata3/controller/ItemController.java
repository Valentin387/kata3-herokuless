package com.kata3.kata3.controller;

import com.kata3.kata3.data.dto.ItemDto;
import com.kata3.kata3.data.entity.Item;
import com.kata3.kata3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestParam(required = false) Item.ItemType type) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = itemService.getItemsByUser(userId, type);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable String id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ItemDto item = itemService.getItemById(id, userId);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ItemDto createdItem = itemService.createItem(itemDto, userId);
        return ResponseEntity.ok(createdItem);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable String id, @RequestBody ItemDto itemDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ItemDto updatedItem = itemService.updateItem(id, itemDto, userId);
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean deleted = itemService.deleteItem(id, userId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}