package com.kata3.kata3.data.dto;

import com.kata3.kata3.data.entity.Item;
import lombok.Data;

@Data
public class ItemDto {
    private String id;
    private Item.ItemType type;
    private String title;
    private String description;
    private boolean finished;
    private String userId;
}
