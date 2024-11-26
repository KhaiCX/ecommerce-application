package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Item;
import com.ecommerceapplication.repository.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;
    @BeforeEach
    public void setup() {
        Item item1 = getItem(1L, "test_item_01");
        Item item2 = getItem(2L, "test_item_XX");
        Item item3 = getItem(3L, "test_item_XX");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findByName(item2.getName())).thenReturn(Lists.list(item2, item3));
        when(itemRepository.findAll()).thenReturn(Lists.list(item1, item2, item3));
    }
    @Test
    public void findAllItems() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        int codeValue = responseEntity.getStatusCode().value();
        List<Item> items = responseEntity.getBody();
        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(items);
        assertEquals(items.size(), 3);
    }
    @Test
    public void findItemById() {
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        int codeValue = responseEntity.getStatusCode().value();
        Item item = responseEntity.getBody();
        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(item);
        assertEquals(item.getItemId().longValue(), 1L);
    }
    @Test
    public void findItemByNonExistentId() {
        ResponseEntity<Item> responseEntity = itemController.getItemById(4L);
        int codeValue = responseEntity.getStatusCode().value();
        assertEquals(codeValue, HttpStatus.NOT_FOUND.value());
    }
    @Test
    public void findItemsByName() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test_item_XX");
        int codeValue = responseEntity.getStatusCode().value();
        List<Item> items = responseEntity.getBody();
        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(items);
        assertEquals(items.size(), 2);
    }
    @Test
    public void findItemsByNonExistentName() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("");
        int codeValue = responseEntity.getStatusCode().value();
        assertEquals(codeValue, HttpStatus.NOT_FOUND.value());
    }
    private static Item getItem(long id, String name) {
        Item item = new Item();
        item.setItemId(id);
        item.setName(name);
        return item;
    }
}
