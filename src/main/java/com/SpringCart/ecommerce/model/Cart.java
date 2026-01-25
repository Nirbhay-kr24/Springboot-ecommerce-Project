package com.SpringCart.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Set<CartItem> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* ================= BUSINESS LOGIC ================= */

    public void addItem(CartItem item) {
        item.setCart(this);
        item.setTotalPrice();
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
        recalculateTotal();
    }

    public void clearItems() {
        this.items.clear();
        this.totalAmount = BigDecimal.ZERO;
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
