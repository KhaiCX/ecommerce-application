package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Cart;
import com.ecommerceapplication.entity.Item;
import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.repository.CartRepository;
import com.ecommerceapplication.repository.ItemRepository;
import com.ecommerceapplication.repository.UserRepository;
import com.ecommerceapplication.request.ModifyCartRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CartControllerTest {
    private static final String USERNAME = "username";
    private static final long ITEM_ID = 1L;
    private static final int QUANTITY = 2;
    private static final String PRICE = "21.45";

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        when(userRepository.findByUsername(anyString())).thenAnswer(answer -> {
            String username = answer.getArgument(0);
            if (USERNAME.equals(username)) {
                return getUser();
            }
            return null;
        });
        when(itemRepository.findById(ITEM_ID)).thenReturn(getItem());
    }

    @Test
    public void addToCart() {
        int expectedQuantity = QUANTITY + 1;
        BigDecimal expectedTotal = new BigDecimal(PRICE).multiply(BigDecimal.valueOf(expectedQuantity));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        Cart cart = response.getBody();

        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertNotNull(cart);
        assertEquals(cart.getUser().getUsername(), USERNAME);
        assertEquals(cart.getItems().size(), expectedQuantity);
        assertEquals(cart.getTotal(), expectedTotal);
    }

    @Test
    public void addToCartRequestHasInvalidUsername() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("");
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void addToCartInvalidItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Cart cart = response.getBody();

        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertNotNull(cart);
        assertEquals(cart.getUser().getUsername(), USERNAME);
        assertEquals(cart.getItems().size(), 0);
        assertEquals(cart.getTotal().intValue(), 0);
    }

    @Test
    public void removeFromCartRequestHasInvalidUsername() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("");
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void removeFromCartInvalidItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USERNAME);
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(QUANTITY);

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }
    private static User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setCart(getCart(user));
        return user;
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
}
