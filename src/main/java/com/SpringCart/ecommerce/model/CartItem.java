package com.SpringCart.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /* ========= BUSINESS METHOD ========= */
    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(
                BigDecimal.valueOf(this.quantity)
        );
    }

}
