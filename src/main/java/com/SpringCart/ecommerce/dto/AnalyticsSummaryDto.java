package com.SpringCart.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryDto {
    private Long totalUsers;
    private Long totalProducts;
    private Long totalOrders;
    private Double totalRevenue;
}