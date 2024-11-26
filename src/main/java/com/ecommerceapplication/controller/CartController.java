package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.Cart;
import com.ecommerceapplication.entity.Item;
import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.repository.CartRepository;
import com.ecommerceapplication.repository.ItemRepository;
import com.ecommerceapplication.repository.UserRepository;
import com.ecommerceapplication.request.ModifyCartRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        logger.info("Start adding item(s) to cart of user '{}'", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if(item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item.get()));
        cartRepository.save(cart);
        logger.info("Item(s) successfully added to cart of user '{}'", request.getUsername());
        logger.info("Start adding item(s) to cart of user '{}'", request.getUsername());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        logger.info("Start removing item(s) from cart of user '{}'", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if(item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item.get()));
        cartRepository.save(cart);
        logger.info("Item(s) successfully removed from cart of user '{}'", request.getUsername());
        logger.info("End removing item(s) from cart of user '{}'", request.getUsername());
        return ResponseEntity.ok(cart);
    }
}
