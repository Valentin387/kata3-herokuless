package com.kata3.kata3.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@Data
public class Item {
    @Id
    private String id;
    private ItemType type;
    private String title;
    private String description;
    private boolean finished;
    private String userId;

    public enum ItemType {
        PROJECT, TASK
    }
}