package com.SpringCart.ecommerce.service.cart;

import com.SpringCart.ecommerce.model.Cart;
import com.SpringCart.ecommerce.model.User;
import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    // ✅ This must accept a User object
    Long initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
    Cart addProductToCart(Long cartId, Long productId, int quantity);
}