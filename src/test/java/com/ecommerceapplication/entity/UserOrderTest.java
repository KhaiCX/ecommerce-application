package com.ecommerceapplication.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserOrderTest {
    @Test
    public void testGetId() {
        UserOrder userOrder = new UserOrder();
        userOrder.setUserOrderId(23L);
        assertEquals(23L, userOrder.getUserOrderId());
    }
}
