package com.kata3.kata3.data.repository;

import com.kata3.kata3.data.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByUserId(String userId);
    List<Item> findByUserIdAndType(String userId, Item.ItemType type);
}
