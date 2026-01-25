package com.SpringCart.ecommerce.request;

import com.SpringCart.ecommerce.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {

    private Long id;
    private String name;
    private String brand;

    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Inventory cannot be negative")
    private int inventory;

    private String description;
    private Category category;
}
