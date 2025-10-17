package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.OrderDTO;
import com.iuh.fit.se.domain.entity.Order;
import com.iuh.fit.se.domain.entity.User;
import com.iuh.fit.se.domain.enums.OrderStatus;
import com.iuh.fit.se.repository.OrderRepository;
import com.iuh.fit.se.repository.UserRepository;
import com.iuh.fit.se.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setPaymentFee(orderDTO.getPaymentFee());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setCreatedDate(orderDTO.getCreatedDate());
        order.setDeliveryAddress(orderDTO.getDeliveryAddress());
        order.setStatus(orderDTO.getStatus());
        order.setNotes(orderDTO.getNotes());
        order.setUsedPoint(orderDTO.getUsedPoint());
        order.setUsedAt(orderDTO.getUsedAt());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        
        if (orderDTO.getUserId() != null) {
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByCreatedDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByMinimumTotal(Double minTotal) {
        return orderRepository.findByMinimumTotal(minTotal).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserAndStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserAndStatus(userId, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        Double revenue = orderRepository.calculateRevenue(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByPaymentMethod(com.iuh.fit.se.domain.enums.PaymentMethod paymentMethod) {
        return orderRepository.findByPaymentMethod(paymentMethod).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getRecentOrders(org.springframework.data.domain.Pageable pageable) {
        return orderRepository.findRecentOrders(pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersWithTotalGreaterThan(Double minTotal) {
        return orderRepository.findByMinimumTotal(minTotal).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setPaymentFee(order.getPaymentFee());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedDate(order.getCreatedDate());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setStatus(order.getStatus());
        dto.setNotes(order.getNotes());
        dto.setUsedPoint(order.getUsedPoint());
        dto.setUsedAt(order.getUsedAt());
        dto.setPaymentMethod(order.getPaymentMethod());
        
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }
        
        return dto;
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setPaymentFee(dto.getPaymentFee());
        order.setTotalPrice(dto.getTotalPrice());
        order.setCreatedDate(dto.getCreatedDate());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setStatus(dto.getStatus());
        order.setNotes(dto.getNotes());
        order.setUsedPoint(dto.getUsedPoint());
        order.setUsedAt(dto.getUsedAt());
        order.setPaymentMethod(dto.getPaymentMethod());
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
        
        return order;
    }
}
