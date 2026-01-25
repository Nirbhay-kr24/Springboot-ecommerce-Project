package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.exceptions.AlreadyExistsException;
import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.Category;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("Found!", categories));
    }



    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Found", category));
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse("Found", category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        Category saved = categoryService.addCategory(category);
        return ResponseEntity.ok(new ApiResponse("Success", saved));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {

        Category updated = categoryService.updateCategory(category, id);
        return ResponseEntity.ok(new ApiResponse("Update success!", updated));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

}
