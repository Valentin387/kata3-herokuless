package com.kata3.kata3.service;

import com.kata3.kata3.data.dto.ItemDto;
import com.kata3.kata3.data.entity.Item;
import com.kata3.kata3.data.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setType(Item.ItemType.PROJECT);
        itemDto.setTitle("My Project");
        itemDto.setDescription("A test project");
        itemDto.setFinished(false);

        item = new Item();
        item.setId("67890");
        item.setType(Item.ItemType.PROJECT);
        item.setTitle("My Project");
        item.setDescription("A test project");
        item.setFinished(false);
        item.setUserId("12345");
    }

    @Test
    void createItem_success() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto createdItem = itemService.createItem(itemDto, "12345");

        assertNotNull(createdItem);
        assertEquals("67890", createdItem.getId());
        assertEquals(Item.ItemType.PROJECT, createdItem.getType());
        assertEquals("My Project", createdItem.getTitle());
        assertEquals("A test project", createdItem.getDescription());
        assertFalse(createdItem.isFinished());
        assertEquals("12345", createdItem.getUserId());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void getItemsByUser_noTypeFilter_returnsAllItems() {
        when(itemRepository.findByUserId("12345")).thenReturn(Arrays.asList(item));

        List<ItemDto> items = itemService.getItemsByUser("12345", null);

        assertEquals(1, items.size());
        ItemDto result = items.get(0);
        assertEquals("67890", result.getId());
        assertEquals(Item.ItemType.PROJECT, result.getType());
        assertEquals("My Project", result.getTitle());
        assertEquals("A test project", result.getDescription());
        assertFalse(result.isFinished());
        assertEquals("12345", result.getUserId());
        verify(itemRepository).findByUserId("12345");
    }

    @Test
    void getItemsByUser_withTypeFilter_returnsFilteredItems() {
        when(itemRepository.findByUserIdAndType("12345", Item.ItemType.PROJECT)).thenReturn(Arrays.asList(item));

        List<ItemDto> items = itemService.getItemsByUser("12345", Item.ItemType.PROJECT);

        assertEquals(1, items.size());
        ItemDto result = items.get(0);
        assertEquals("67890", result.getId());
        assertEquals(Item.ItemType.PROJECT, result.getType());
        assertEquals("12345", result.getUserId());
        verify(itemRepository).findByUserIdAndType("12345", Item.ItemType.PROJECT);
    }

    @Test
    void getItemsByUser_noItems_returnsEmptyList() {
        when(itemRepository.findByUserId("12345")).thenReturn(Arrays.asList());

        List<ItemDto> items = itemService.getItemsByUser("12345", null);

        assertTrue(items.isEmpty());
        verify(itemRepository).findByUserId("12345");
    }
}