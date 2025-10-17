package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.ProductReviewDTO;
import com.iuh.fit.se.domain.entity.Product;
import com.iuh.fit.se.domain.entity.ProductReview;
import com.iuh.fit.se.repository.ProductRepository;
import com.iuh.fit.se.repository.ProductReviewRepository;
import com.iuh.fit.se.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReviewServiceImpl implements ProductReviewService {
    
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductReviewDTO createReview(ProductReviewDTO reviewDTO) {
        ProductReview review = convertToEntity(reviewDTO);
        review.setCreatedAt(LocalDateTime.now());
        ProductReview savedReview = productReviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Override
    public ProductReviewDTO updateReview(Long id, ProductReviewDTO reviewDTO) {
        ProductReview review = productReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        
        if (reviewDTO.getProductId() != null) {
            Product product = productRepository.findById(reviewDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            review.setProduct(product);
        }
        
        ProductReview updatedReview = productReviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    @Override
    public void deleteReview(Long id) {
        productReviewRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReviewDTO getReviewById(Long id) {
        ProductReview review = productReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        return convertToDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDTO> getAllReviews() {
        return productReviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDTO> getReviewsByProduct(Long productId) {
        return productReviewRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDTO> getReviewsByMinimumRating(int minRating) {
        return productReviewRepository.findByMinimumRating(minRating).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByProduct(Long productId) {
        Double avg = productReviewRepository.getAverageRatingByProduct(productId);
        return avg != null ? avg : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDTO> getRecentReviews(org.springframework.data.domain.Pageable pageable) {
        return productReviewRepository.findRecentReviews(pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countReviewsByProduct(Long productId) {
        return productReviewRepository.countByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getTopRatedProducts(org.springframework.data.domain.Pageable pageable) {
        return productReviewRepository.findTopRatedProducts(4.0, pageable);
    }

    private ProductReviewDTO convertToDTO(ProductReview review) {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        
        if (review.getProduct() != null) {
            dto.setProductId(review.getProduct().getId());
        }
        
        return dto;
    }

    private ProductReview convertToEntity(ProductReviewDTO dto) {
        ProductReview review = new ProductReview();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            review.setProduct(product);
        }
        
        return review;
    }
}
