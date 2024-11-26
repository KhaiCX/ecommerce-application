package com.ecommerceapplication.controller;

import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.entity.UserOrder;
import com.ecommerceapplication.repository.OrderRepository;
import com.ecommerceapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;


    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        logger.info("Start submitting order for user '{}'", username);
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("Order was successfully submitted for user '{}'", username);
        logger.info("End submitting order for user '{}'", username);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        logger.info("Start getting orders for user '{}'", username);
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        logger.info("Orders successfully retrieved for user '{}'", username);
        logger.info("End getting orders for user '{}'", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
