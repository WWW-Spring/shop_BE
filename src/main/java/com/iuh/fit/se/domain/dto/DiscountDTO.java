package com.iuh.fit.se.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountDTO {
    Long id;
    String name;
    String type;
    String description;
    Double discountRate;
    Boolean discountStatus;
    LocalDate startDate;
    LocalDate endDate;
    Long productId;
}
