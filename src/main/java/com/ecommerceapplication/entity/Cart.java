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
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long cartId;
    @JsonProperty
    private BigDecimal total;
    @ManyToMany
    @JsonProperty
    private List<Item> items;
    @JsonProperty
    @OneToOne(mappedBy = "cart")
    private User user;
    public void addItem(Item item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.add(item.getPrice());
    }
    public void removeItem(Item item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.remove(item);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.subtract(item.getPrice());
    }
}
