package com.iuh.fit.se.domain.dto;

import com.iuh.fit.se.domain.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    Long id;
    String name;
    String description;
    Double unitPrice;
    String imageUrl;
    Long quantity;
    Double percentage;
    ProductStatus status;
    Long categoryId;
}
