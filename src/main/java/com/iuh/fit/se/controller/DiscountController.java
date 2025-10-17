package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.DiscountDTO;
import com.iuh.fit.se.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiscountController {
    
    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountDTO> createDiscount(@RequestBody DiscountDTO discountDTO) {
        DiscountDTO createdDiscount = discountService.createDiscount(discountDTO);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountDTO> updateDiscount(@PathVariable Long id, @RequestBody DiscountDTO discountDTO) {
        DiscountDTO updatedDiscount = discountService.updateDiscount(id, discountDTO);
        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscountById(@PathVariable Long id) {
        DiscountDTO discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        List<DiscountDTO> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<DiscountDTO> getDiscountByProduct(@PathVariable Long productId) {
        DiscountDTO discount = discountService.getDiscountByProduct(productId);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DiscountDTO>> getDiscountsByStatus(@PathVariable Boolean status) {
        List<DiscountDTO> discounts = discountService.getDiscountsByStatus(status);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DiscountDTO>> getActiveDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DiscountDTO> discounts = discountService.getActiveDiscounts(date);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<DiscountDTO>> getExpiredDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DiscountDTO> discounts = discountService.getExpiredDiscounts(date);
        return ResponseEntity.ok(discounts);
    }

    @PatchMapping("/{discountId}/activate")
    public ResponseEntity<Void> activateDiscount(@PathVariable Long discountId) {
        discountService.activateDiscount(discountId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{discountId}/deactivate")
    public ResponseEntity<Void> deactivateDiscount(@PathVariable Long discountId) {
        discountService.deactivateDiscount(discountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<DiscountDTO>> getDiscountsByType(@PathVariable String type) {
        List<DiscountDTO> discounts = discountService.getDiscountsByType(type);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<DiscountDTO>> getUpcomingDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DiscountDTO> discounts = discountService.getUpcomingDiscounts(fromDate, toDate);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/rate-range")
    public ResponseEntity<List<DiscountDTO>> getDiscountsByRateRange(
            @RequestParam Double minRate,
            @RequestParam Double maxRate) {
        List<DiscountDTO> discounts = discountService.getDiscountsByRateRange(minRate, maxRate);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/best")
    public ResponseEntity<List<DiscountDTO>> getBestActiveDiscounts() {
        List<DiscountDTO> discounts = discountService.getBestActiveDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveDiscounts() {
        Long count = discountService.countActiveDiscounts();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/deactivate-expired")
    public ResponseEntity<Integer> deactivateExpiredDiscounts() {
        int count = discountService.deactivateExpiredDiscounts();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<DiscountDTO>> getDiscountsByCategory(@PathVariable Long categoryId) {
        List<DiscountDTO> discounts = discountService.getDiscountsByCategory(categoryId);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DiscountDTO>> searchDiscountsByKeyword(@RequestParam String keyword) {
        List<DiscountDTO> discounts = discountService.searchDiscountsByKeyword(keyword);
        return ResponseEntity.ok(discounts);
    }
}
