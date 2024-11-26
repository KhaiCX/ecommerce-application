package com.ecommerceapplication.repository;

import com.ecommerceapplication.entity.Cart;
import com.ecommerceapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
