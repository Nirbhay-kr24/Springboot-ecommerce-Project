package com.SpringCart.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCartApplication.class, args);
    }
}