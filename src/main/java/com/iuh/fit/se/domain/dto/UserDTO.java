package com.iuh.fit.se.domain.dto;

import com.iuh.fit.se.domain.enums.Gender;
import com.iuh.fit.se.domain.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    Long id;
    String fullName;
    String password;
    String confirmPassword;
    String email;
    Gender gender;
    String address;
    String phoneNumber;
    LocalDate dob;
    UserStatus status;
    Long cumulativePoints;
    Long roleId;
}
