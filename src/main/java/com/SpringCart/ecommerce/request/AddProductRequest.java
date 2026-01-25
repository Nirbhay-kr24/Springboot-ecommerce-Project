package com.SpringCart.ecommerce.request;

import com.SpringCart.ecommerce.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Inventory cannot be negative")
    private int inventory;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category is required")
    private Category category;
}
