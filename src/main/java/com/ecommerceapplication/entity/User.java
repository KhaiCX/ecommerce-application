package com.ecommerceapplication.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long userId;
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @OneToOne
    @JoinColumn(name = "cart_id")
    @JsonProperty
    private Cart cart;
}
