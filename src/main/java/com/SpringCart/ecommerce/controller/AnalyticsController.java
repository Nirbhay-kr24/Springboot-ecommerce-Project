package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.dto.AnalyticsSummaryDto;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.service.analytics.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getSummary() {
        AnalyticsSummaryDto summary = new AnalyticsSummaryDto(
                analyticsService.getTotalUsers(),
                analyticsService.getTotalProducts(),
                analyticsService.getTotalOrders(),
                analyticsService.getTotalRevenue()
        );
        return ResponseEntity.ok(new ApiResponse("Analytics summary fetched", summary));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse> getRevenue() {
        return ResponseEntity.ok(new ApiResponse("Total revenue fetched", analyticsService.getTotalRevenue()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders-per-day")
    public ResponseEntity<ApiResponse> getOrdersPerDay() {
        return ResponseEntity.ok(new ApiResponse("Orders per day fetched", analyticsService.getOrdersPerDay()));
    }
}