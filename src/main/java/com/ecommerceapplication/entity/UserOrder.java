package com.ecommerceapplication.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long userOrderId;
    @ManyToMany
    @JsonProperty
    private List<Item> items;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty
    private User user;
    private BigDecimal total;
    public static UserOrder createFromCart(Cart cart) {
        UserOrder order = new UserOrder();
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        return order;
    }
}
