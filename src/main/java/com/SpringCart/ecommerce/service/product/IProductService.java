package com.SpringCart.ecommerce.service.product;

import com.SpringCart.ecommerce.dto.ProductDto;
import com.SpringCart.ecommerce.model.Product;
import com.SpringCart.ecommerce.request.AddProductRequest;
import com.SpringCart.ecommerce.request.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {

    // ---------- BASIC CRUD ----------
    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(ProductUpdateRequest product, Long productId);

    List<Product> getAllProducts();

    // ---------- FILTERING ----------
    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    // ---------- ANALYTICS ----------
    Long countProductsByBrandAndName(String brand, String name);

    // ---------- DTO CONVERSION ----------
    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);

    // ---------- PAGINATION ----------
    Page<Product> getAllProducts(Pageable pageable);

    // ---------- ADVANCED SEARCH (STEP 3 CORE METHOD) ----------
    Page<Product> searchProducts(
            String name,
            String brand,
            String category,
            Pageable pageable
    );
}
