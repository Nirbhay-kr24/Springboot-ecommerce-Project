package com.SpringCart.ecommerce.service.cart;

import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.Cart;
import com.SpringCart.ecommerce.model.CartItem;
import com.SpringCart.ecommerce.model.Product;
import com.SpringCart.ecommerce.model.User;
import com.SpringCart.ecommerce.repository.CartRepository;
import com.SpringCart.ecommerce.repository.ProductRepository;
import com.SpringCart.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getItems().clear(); // Fix: Clear the items set
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    // ✅ ADDED: This satisfies the interface requirement
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart(User user) {
        return Optional.ofNullable(cartRepository.findByUserId(user.getId()))
                .map(Cart::getId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user); // ✅ Correctly links Cart to User
                    return cartRepository.save(cart).getId();
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cart.getItems()
                .stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
            item.setCart(cart);
            cart.addItem(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        item.setTotalPrice();
        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }
}