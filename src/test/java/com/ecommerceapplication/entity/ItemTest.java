package com.ecommerceapplication.entity;

import com.ecommerceapplication.controller.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    @Test
    public void testEquals() {
        Item item = TestUtils.getItem0();
        assertNotEquals(item, null);
        String str = "bob";
        assertNotEquals(str, item);
        Item item2 = TestUtils.getItem1();
        assertNotEquals(item, item2);
        item.setItemId(null);
        assertNotEquals(item, item2);
    }

    @Test
    public void testEquals_Symmetric() {
        Item item1 = new Item();
        item1.setItemId(0L);
        Item item2 = new Item();
        item2.setItemId(0L);
        assertTrue(item1.equals(item2) && item2.equals(item1));
        assertEquals(item2.hashCode(), item1.hashCode());
    }
}
