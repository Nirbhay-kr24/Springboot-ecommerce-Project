package com.SpringCart.ecommerce.service.order;

import com.SpringCart.ecommerce.dto.OrderDto;
import com.SpringCart.ecommerce.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
