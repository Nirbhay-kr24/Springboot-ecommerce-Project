package com.SpringCart.ecommerce.repository;

import com.SpringCart.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o")
    Double getTotalRevenue();

    @Query("SELECT o.orderDate, COUNT(o) FROM Order o GROUP BY o.orderDate ORDER BY o.orderDate")
    List<Object[]> getOrdersPerDay();
}