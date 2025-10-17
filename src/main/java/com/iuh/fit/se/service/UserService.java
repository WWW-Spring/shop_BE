package com.iuh.fit.se.service;

import com.iuh.fit.se.domain.dto.UserDTO;
import com.iuh.fit.se.domain.enums.UserStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO getUserByEmail(String email);
    List<UserDTO> getUsersByStatus(UserStatus status);
    List<UserDTO> getUsersByRole(Long roleId);
    List<UserDTO> searchUsersByName(String name);
    List<UserDTO> getUsersByMinimumPoints(Long points);
    void updateUserPoints(Long userId, Long points);
    
    // Advanced methods
    Long countUsersByStatus(UserStatus status);
    List<UserDTO> getTopCustomersByPoints(Pageable pageable);
}
