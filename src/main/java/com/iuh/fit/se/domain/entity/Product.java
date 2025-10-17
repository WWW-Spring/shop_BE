package com.iuh.fit.se.domain.entity;

import com.iuh.fit.se.domain.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Product
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 05/09/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 05/09/2025    Hai Dang      Create
 */
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Double unitPrice;
    String imageUrl;
    Long quantity;
    Double percentage;
    @Enumerated(EnumType.STRING)
    ProductStatus status;

    @OneToMany(mappedBy = "product")
    Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    Set<ProductReview> productReviews;

    @OneToOne(mappedBy = "product")
    Discount discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
