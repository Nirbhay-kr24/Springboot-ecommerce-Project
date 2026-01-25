package com.SpringCart.ecommerce.service.analytics;

import java.util.List;

public interface AnalyticsService {

    Long getTotalUsers();

    Long getTotalProducts();

    Long getTotalOrders();

    Double getTotalRevenue();


    List<Object[]> getOrdersPerDay();
}
