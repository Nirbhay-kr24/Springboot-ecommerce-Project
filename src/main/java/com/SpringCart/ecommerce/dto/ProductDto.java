package com.SpringCart.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;


    private Long categoryId;
    private String categoryName;

    private List<ImageDto> images;
}
