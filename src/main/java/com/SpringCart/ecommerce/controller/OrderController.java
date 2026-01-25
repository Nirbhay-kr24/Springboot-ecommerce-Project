package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.dto.OrderDto;
import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.Order;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}") // Path: /api/orders/order/1
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order found!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Oops!", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}") // Path: /api/orders/user/1
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> order = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("User orders fetched!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Oops!", e.getMessage()));
        }
    }
}