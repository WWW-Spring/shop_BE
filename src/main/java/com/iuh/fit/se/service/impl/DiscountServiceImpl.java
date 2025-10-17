package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.DiscountDTO;
import com.iuh.fit.se.domain.entity.Discount;
import com.iuh.fit.se.domain.entity.Product;
import com.iuh.fit.se.repository.DiscountRepository;
import com.iuh.fit.se.repository.ProductRepository;
import com.iuh.fit.se.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountServiceImpl implements DiscountService {
    
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Discount discount = convertToEntity(discountDTO);
        Discount savedDiscount = discountRepository.save(discount);
        return convertToDTO(savedDiscount);
    }

    @Override
    public DiscountDTO updateDiscount(Long id, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));
        
        discount.setName(discountDTO.getName());
        discount.setType(discountDTO.getType());
        discount.setDescription(discountDTO.getDescription());
        discount.setDiscountRate(discountDTO.getDiscountRate());
        discount.setDiscountStatus(discountDTO.getDiscountStatus());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        
        if (discountDTO.getProductId() != null) {
            Product product = productRepository.findById(discountDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            discount.setProduct(product);
        }
        
        Discount updatedDiscount = discountRepository.save(discount);
        return convertToDTO(updatedDiscount);
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountDTO getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));
        return convertToDTO(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountDTO getDiscountByProduct(Long productId) {
        Discount discount = discountRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Discount not found for product: " + productId));
        return convertToDTO(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getDiscountsByStatus(Boolean status) {
        return discountRepository.findByDiscountStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getActiveDiscounts(LocalDate date) {
        return discountRepository.findActiveDiscounts(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getExpiredDiscounts(LocalDate date) {
        return discountRepository.findExpiredDiscounts(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void activateDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setDiscountStatus(true);
        discountRepository.save(discount);
    }

    @Override
    public void deactivateDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setDiscountStatus(false);
        discountRepository.save(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getDiscountsByType(String type) {
        return discountRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getUpcomingDiscounts(LocalDate fromDate, LocalDate toDate) {
        return discountRepository.findUpcomingDiscounts(fromDate, toDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getDiscountsByRateRange(Double minRate, Double maxRate) {
        return discountRepository.findByDiscountRateRange(minRate, maxRate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getBestActiveDiscounts() {
        return discountRepository.findBestActiveDiscounts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveDiscounts() {
        return discountRepository.countActiveDiscounts();
    }

    @Override
    public int deactivateExpiredDiscounts() {
        return discountRepository.deactivateExpiredDiscounts();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> getDiscountsByCategory(Long categoryId) {
        return discountRepository.findActiveByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO> searchDiscountsByKeyword(String keyword) {
        return discountRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DiscountDTO convertToDTO(Discount discount) {
        DiscountDTO dto = new DiscountDTO();
        dto.setId(discount.getId());
        dto.setName(discount.getName());
        dto.setType(discount.getType());
        dto.setDescription(discount.getDescription());
        dto.setDiscountRate(discount.getDiscountRate());
        dto.setDiscountStatus(discount.getDiscountStatus());
        dto.setStartDate(discount.getStartDate());
        dto.setEndDate(discount.getEndDate());
        
        if (discount.getProduct() != null) {
            dto.setProductId(discount.getProduct().getId());
        }
        
        return dto;
    }

    private Discount convertToEntity(DiscountDTO dto) {
        Discount discount = new Discount();
        discount.setName(dto.getName());
        discount.setType(dto.getType());
        discount.setDescription(dto.getDescription());
        discount.setDiscountRate(dto.getDiscountRate());
        discount.setDiscountStatus(dto.getDiscountStatus());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            discount.setProduct(product);
        }
        
        return discount;
    }
}
