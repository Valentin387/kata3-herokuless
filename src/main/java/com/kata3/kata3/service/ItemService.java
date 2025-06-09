package com.kata3.kata3.service;

import com.kata3.kata3.data.dto.ItemDto;
import com.kata3.kata3.data.entity.Item;
import com.kata3.kata3.data.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<ItemDto> getItemsByUser(String userId, Item.ItemType type) {
        List<Item> items = type != null ? itemRepository.findByUserIdAndType(userId, type) : itemRepository.findByUserId(userId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ItemDto getItemById(String id, String userId) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent() && item.get().getUserId().equals(userId)) {
            return convertToDto(item.get());
        }
        return null;
    }

    public ItemDto createItem(ItemDto itemDto, String userId) {
        Item item = new Item();
        item.setType(itemDto.getType());
        item.setTitle(itemDto.getTitle());
        item.setDescription(itemDto.getDescription());
        item.setFinished(itemDto.isFinished());
        item.setUserId(userId);
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    public ItemDto updateItem(String id, ItemDto itemDto, String userId) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent() && optionalItem.get().getUserId().equals(userId)) {
            Item item = optionalItem.get();
            if (itemDto.getType() != null) item.setType(itemDto.getType());
            if (itemDto.getTitle() != null) item.setTitle(itemDto.getTitle());
            if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
            item.setFinished(itemDto.isFinished());
            Item updatedItem = itemRepository.save(item);
            return convertToDto(updatedItem);
        }
        return null;
    }

    public boolean deleteItem(String id, String userId) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent() && optionalItem.get().getUserId().equals(userId)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setType(item.getType());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setFinished(item.isFinished());
        dto.setUserId(item.getUserId());
        return dto;
    }
}