package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Item;

import java.math.BigDecimal;

public class TestUtils {
    public static final String ROUND_WIDGET = "Round Widget";

    public static Item getItem0() {
        Item item = new Item();
        item.setItemId(0L);
        item.setName(ROUND_WIDGET);
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("A widget that is round");
        return item;
    }

    public static Item getItem1() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Square Widget");
        item.setPrice(new BigDecimal("1.99"));
        item.setDescription("A widget that is square");
        return item;
    }
}
