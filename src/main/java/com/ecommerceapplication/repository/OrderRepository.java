package com.ecommerceapplication.repository;

import com.ecommerceapplication.entity.User;
import com.ecommerceapplication.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    List<UserOrder> findByUser(User user);
}
