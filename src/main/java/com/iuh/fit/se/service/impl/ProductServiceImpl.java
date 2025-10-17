package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.ProductDTO;
import com.iuh.fit.se.domain.entity.Category;
import com.iuh.fit.se.domain.entity.Product;
import com.iuh.fit.se.domain.enums.ProductStatus;
import com.iuh.fit.se.repository.CategoryRepository;
import com.iuh.fit.se.repository.ProductRepository;
import com.iuh.fit.se.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setQuantity(productDTO.getQuantity());
        product.setPercentage(productDTO.getPercentage());
        product.setStatus(productDTO.getStatus());

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setQuantity(product.getQuantity());
        dto.setPercentage(product.getPercentage());
        dto.setStatus(product.getStatus());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }

        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setQuantity(dto.getQuantity());
        product.setPercentage(dto.getPercentage());
        product.setStatus(dto.getStatus());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return product;
    }

}
