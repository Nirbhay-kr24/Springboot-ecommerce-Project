package com.SpringCart.ecommerce.service.analytics;

import com.SpringCart.ecommerce.repository.OrderRepository;
import com.SpringCart.ecommerce.repository.ProductRepository;
import com.SpringCart.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public Long getTotalProducts() {
        return productRepository.count();
    }

    @Override
    public Long getTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public Double getTotalRevenue() {
        // ✅ Ensure OrderRepository has: Double getTotalRevenue();
        return orderRepository.getTotalRevenue();
    }

    @Override
    public List<Object[]> getOrdersPerDay() {
        // ✅ Ensure OrderRepository has: List<Object[]> getOrdersPerDay();
        return orderRepository.getOrdersPerDay();
    }
}