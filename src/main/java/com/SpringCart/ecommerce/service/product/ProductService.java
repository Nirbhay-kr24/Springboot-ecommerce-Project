package com.SpringCart.ecommerce.service.product;

import com.SpringCart.ecommerce.dto.ImageDto;
import com.SpringCart.ecommerce.dto.ProductDto;
import com.SpringCart.ecommerce.exceptions.ResourceNotFoundException;
import com.SpringCart.ecommerce.model.Category;
import com.SpringCart.ecommerce.model.Image;
import com.SpringCart.ecommerce.model.Product;
import com.SpringCart.ecommerce.repository.CategoryRepository;
import com.SpringCart.ecommerce.repository.ImageRepository;
import com.SpringCart.ecommerce.repository.ProductRepository;
import com.SpringCart.ecommerce.request.AddProductRequest;
import com.SpringCart.ecommerce.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    // ---------------- ADD PRODUCT ----------------
    @CacheEvict(value = "products", allEntries = true)
    @Override
    public Product addProduct(AddProductRequest request) {

        String categoryName = request.getCategory().getName();

        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    return categoryRepository.save(newCategory);
                });

        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    // ---------------- GET BY ID ----------------
    @Cacheable(value = "products", key = "#id")
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }


    // ---------------- DELETE ----------------
    @CacheEvict(value = "products", allEntries = true)
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> { throw new ResourceNotFoundException("Product not found!"); }
                );
    }

    // ---------------- UPDATE ----------------
    @CacheEvict(value = "products", allEntries = true)
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        if (request.getName() != null) {
            existingProduct.setName(request.getName());
        }

        if (request.getBrand() != null) {
            existingProduct.setBrand(request.getBrand());
        }

        if (request.getPrice() != null) {
            existingProduct.setPrice(request.getPrice());
        }

        if (request.getInventory() >= 0) {
            existingProduct.setInventory(request.getInventory());
        }

        if (request.getDescription() != null) {
            existingProduct.setDescription(request.getDescription());
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository
                    .findByName(request.getCategory().getName())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        return existingProduct;
    }

    // ---------------- BASIC FILTERING ----------------
    @Cacheable(value = "products")
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    // ---------------- ANALYTICS ----------------
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    // ---------------- DTO CONVERSION ----------------
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    // ---------------- PAGINATION ----------------
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ---------------- ADVANCED SEARCH (STEP 3 CORE) ----------------
    @Override
    public Page<Product> searchProducts(
            String name,
            String brand,
            String category,
            Pageable pageable) {

        return productRepository.searchProducts(
                name,
                brand,
                category,
                pageable
        );
    }
}
