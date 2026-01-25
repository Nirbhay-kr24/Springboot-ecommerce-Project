package com.SpringCart.ecommerce.service.order;

import com.SpringCart.ecommerce.dto.OrderDto;
import com.SpringCart.ecommerce.enums.OrderStatus;
import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.*;
import com.SpringCart.ecommerce.repository.OrderRepository;
import com.SpringCart.ecommerce.repository.ProductRepository;
import com.SpringCart.ecommerce.service.cart.ICartService;
import com.SpringCart.ecommerce.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final EmailService emailService; // ✅ Inject EmailService

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cannot place order: Cart is empty or not found for user ID: " + userId);
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);

        Set<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(orderItems);

        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        // ✅ Send Confirmation Email after successful order
        sendOrderConfirmationEmail(savedOrder);

        return savedOrder;
    }

    private void sendOrderConfirmationEmail(Order order) {
        String to = order.getUser().getEmail();

        // ✅ Change .getId() to .getOrderId()
        String subject = "Order Confirmation - #" + order.getOrderId();

        String itemsList = order.getOrderItems().stream()
                .map(item -> "- " + item.getProduct().getName() + " (Qty: " + item.getQuantity() + ")")
                .collect(Collectors.joining("\n"));

        String body = String.format(
                "Hi %s,\n\nThank you for your order! Your order #%d has been placed successfully.\n\n" +
                        "Order Summary:\n%s\n\nTotal Amount: $%s\n\nWe will notify you when your items ship!",
                order.getUser().getFirstName(),
                order.getOrderId(), // ✅ Change .getId() to .getOrderId()
                itemsList,
                order.getTotalAmount()
        );

        emailService.sendEmail(to, subject, body);
    }

    private Set<OrderItem> createOrderItems(Order order, Cart cart) {
        return new HashSet<>(cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            if (product.getInventory() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);

            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList());
    }

    private BigDecimal calculateTotalAmount(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> modelMapper.map(order, OrderDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .toList();
    }
}