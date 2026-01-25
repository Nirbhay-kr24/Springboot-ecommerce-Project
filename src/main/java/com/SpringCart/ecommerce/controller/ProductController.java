package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.dto.ProductDto;
import com.SpringCart.ecommerce.model.Product;
import com.SpringCart.ecommerce.request.AddProductRequest;
import com.SpringCart.ecommerce.request.ProductUpdateRequest;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.service.product.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    // ---------------- GET ALL ----------------
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        return ResponseEntity.ok(
                new ApiResponse("Success", productService.getAllProducts())
        );
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("success", productDto));
    }

    // ---------------- ADD PRODUCT ----------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(
            @Valid @RequestBody AddProductRequest request) {

        return ResponseEntity.ok(
                new ApiResponse("Product added", productService.addProduct(request))
        );
    }

    // ---------------- UPDATE PRODUCT ----------------
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(
            @Valid @RequestBody ProductUpdateRequest request,
            @PathVariable Long productId) {

        Product theProduct = productService.updateProduct(request, productId);
        ProductDto productDto = productService.convertToDto(theProduct);
        return ResponseEntity.ok(new ApiResponse("Update product success!", productDto));
    }

    // ---------------- DELETE PRODUCT ----------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    // ---------------- FILTERING APIS ----------------
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
            @RequestParam String brandName,
            @RequestParam String productName) {

        List<Product> products =
                productService.getProductsByBrandAndName(brandName, productName);

        List<ProductDto> convertedProducts =
                productService.getConvertedProducts(products);

        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(
            @RequestParam String category,
            @RequestParam String brand) {

        List<Product> products =
                productService.getProductsByCategoryAndBrand(category, brand);

        List<ProductDto> convertedProducts =
                productService.getConvertedProducts(products);

        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        List<Product> products = productService.getProductsByName(name);
        List<ProductDto> convertedProducts =
                productService.getConvertedProducts(products);

        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        List<ProductDto> convertedProducts =
                productService.getConvertedProducts(products);

        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductDto> convertedProducts =
                productService.getConvertedProducts(products);

        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    // ---------------- ANALYTICS ----------------
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name) {

        var productCount =
                productService.countProductsByBrandAndName(brand, name);

        return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
    }

    // ---------------- PAGINATION ----------------
    @GetMapping("/page")
    public ResponseEntity<ApiResponse> getProductsWithPagination(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }
}
