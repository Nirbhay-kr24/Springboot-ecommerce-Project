package com.SpringCart.ecommerce.service.category;

import com.SpringCart.ecommerce.exceptions.AlreadyExistsException;
import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.Category;
import com.SpringCart.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with name: " + name));
    }

    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException(
                    "Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category existing = getCategoryById(id);
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
