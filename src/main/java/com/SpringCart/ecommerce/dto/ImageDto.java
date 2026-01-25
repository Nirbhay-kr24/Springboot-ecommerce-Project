package com.SpringCart.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Long id;
    private String downloadUrl;

    public void setImageName(String fileName) {
    }
}

