package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.dto.CartDto;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    // Fix: This matches /api/carts/1
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(
                new ApiResponse("Cart fetched", cartService.getCart(cartId))
        );
    }

    // Fix: This matches /api/carts/1/my-cart
    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getMyCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(
                new ApiResponse("Cart fetched successfully", cartService.getCart(cartId))
        );
    }

    @PostMapping("/{cartId}/add/{productId}")
    public ResponseEntity<ApiResponse> addToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        return ResponseEntity.ok(
                new ApiResponse("Product added",
                        cartService.addProductToCart(cartId, productId, quantity))
        );
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", null));
    }
}
