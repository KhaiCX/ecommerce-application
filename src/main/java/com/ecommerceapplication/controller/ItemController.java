package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Item;
import com.ecommerceapplication.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        logger.info("Start get item by id '{}'", id);
        return ResponseEntity.of(itemRepository.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        logger.info("Start getting items by name '{}'", name);
        List<Item> items = itemRepository.findByName(name);
        if (items == null || items.isEmpty()) {
            logger.error("Invalid item. Failed to retrieve items by the name '{}'", name);
            return ResponseEntity.notFound().build();
        }

        logger.info("Successfully retrieved items by the name '{}'.", name);
        logger.info("End getting items by name '{}'", name);
        return ResponseEntity.ok(items);

    }
}
