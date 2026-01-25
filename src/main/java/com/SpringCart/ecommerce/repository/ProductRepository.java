package com.SpringCart.ecommerce.repository;

import com.SpringCart.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ---------- BASIC FILTERING ----------
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    // ---------- ANALYTICS ----------
    Long countByBrandAndName(String brand, String name);

    // ---------- PAGINATION ----------
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategoryName(String category, Pageable pageable);

    Page<Product> findByBrand(String brand, Pageable pageable);

    // ---------- ADVANCED SEARCH (STEP 3 CORE QUERY) ----------
    @Query("""
        SELECT p FROM Product p
        WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
          AND (:category IS NULL OR LOWER(p.category.name) = LOWER(:category))
    """)
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category,
            Pageable pageable
    );
}
