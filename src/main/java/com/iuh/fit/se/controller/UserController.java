package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ApiResponse;
import com.iuh.fit.se.domain.dto.UserDTO;
import com.iuh.fit.se.domain.enums.UserStatus;
import com.iuh.fit.se.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ApiResponse.<UserDTO>builder()
                .result(createdUser)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable UserStatus status) {
        List<UserDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Long roleId) {
        List<UserDTO> users = userService.getUsersByRole(roleId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsersByName(@RequestParam String name) {
        List<UserDTO> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/points")
    public ResponseEntity<List<UserDTO>> getUsersByMinimumPoints(@RequestParam Long points) {
        List<UserDTO> users = userService.getUsersByMinimumPoints(points);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{userId}/points")
    public ResponseEntity<Void> updateUserPoints(@PathVariable Long userId, @RequestParam Long points) {
        userService.updateUserPoints(userId, points);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countUsersByStatus(@PathVariable UserStatus status) {
        Long count = userService.countUsersByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<UserDTO>> getTopCustomersByPoints(@RequestParam(defaultValue = "10") int limit) {
        List<UserDTO> topCustomers = userService.getTopCustomersByPoints(
                org.springframework.data.domain.PageRequest.of(0, limit)
        );
        return ResponseEntity.ok(topCustomers);
    }
}
