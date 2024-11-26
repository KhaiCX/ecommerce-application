package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Cart;
import com.ecommerceapplication.entity.Item;
import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.entity.UserOrder;
import com.ecommerceapplication.repository.OrderRepository;
import com.ecommerceapplication.repository.UserRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderControllerTest {
    private static final String USERNAME = "username";
    private static final long ITEM_ID = 1L;
    private static final String PRICE = "21.45";
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Test
    public void submitUserOrder() {
        ResponseEntity<UserOrder> response = orderController.submit(USERNAME);
        UserOrder userOrder = response.getBody();

        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertNotNull(userOrder);
        assertEquals(userOrder.getUser().getUsername(), USERNAME);
        assertEquals(userOrder.getItems().size(), 1);
        assertEquals(userOrder.getTotal(), new BigDecimal(PRICE));
    }
    @Test
    public void submitUserOrderWithInvalidUsername() {
        ResponseEntity<UserOrder> response = orderController.submit("");
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }
    @Test
    public void getOrdersForUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(USERNAME);
        List<UserOrder> userOrders = response.getBody();

        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertNotNull(userOrders);
        assertEquals(userOrders.size(), 1);

        UserOrder userOrder = userOrders.get(0);
        assertEquals(userOrder.getUser().getUsername(), USERNAME);
        assertEquals(userOrder.getTotal(), new BigDecimal(PRICE));
        assertEquals(userOrder.getItems().size(), 1);
    }

    @Test
    public void getOrdersForInvalidUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("");
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }

    private static User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setCart(getCart(user));
        return user;
    }
    @BeforeEach
    public void setup() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(getUser());
        when(orderRepository.findByUser(any())).thenReturn(getUserOrders());
    }
    private static Cart getCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(getItem().orElse(null));
        return cart;
    }

    private static Optional<Item> getItem() {
        Item item = new Item();
        item.setItemId(ITEM_ID);
        item.setPrice(new BigDecimal(PRICE));
        return Optional.of(item);
    }

    private static List<UserOrder> getUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(getUser().getCart());
        return Lists.list(userOrder);
    }
}
