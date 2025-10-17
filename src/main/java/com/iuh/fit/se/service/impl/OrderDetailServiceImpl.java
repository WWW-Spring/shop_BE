package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.OrderDetailDTO;
import com.iuh.fit.se.domain.entity.Order;
import com.iuh.fit.se.domain.entity.OrderDetail;
import com.iuh.fit.se.domain.entity.Product;
import com.iuh.fit.se.repository.OrderDetailRepository;
import com.iuh.fit.se.repository.OrderRepository;
import com.iuh.fit.se.repository.ProductRepository;
import com.iuh.fit.se.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {
    
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = convertToEntity(orderDetailDTO);
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return convertToDTO(savedOrderDetail);
    }

    @Override
    public OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found with id: " + id));
        
        orderDetail.setAmount(orderDetailDTO.getAmount());
        orderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());
        
        if (orderDetailDTO.getOrderId() != null) {
            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            orderDetail.setOrder(order);
        }
        
        if (orderDetailDTO.getProductId() != null) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderDetail.setProduct(product);
        }
        
        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);
        return convertToDTO(updatedOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailDTO getOrderDetailById(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found with id: " + id));
        return convertToDTO(orderDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getAllOrderDetails() {
        return orderDetailRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByOrder(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByProduct(Long productId) {
        return orderDetailRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByUser(Long userId) {
        return orderDetailRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByStatus(com.iuh.fit.se.domain.enums.OrderStatus status) {
        return orderDetailRepository.findByOrderStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateProductRevenue(Long productId) {
        Double revenue = orderDetailRepository.calculateProductRevenue(productId);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalQuantitySold(Long productId) {
        Long quantity = orderDetailRepository.getTotalQuantitySold(productId);
        return quantity != null ? quantity : 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getBestSellingProducts(org.springframework.data.domain.Pageable pageable) {
        return orderDetailRepository.findBestSellingProducts(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateOrderTotal(Long orderId) {
        Double total = orderDetailRepository.calculateOrderTotal(orderId);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countItemsInOrder(Long orderId) {
        return orderDetailRepository.countItemsInOrder(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getPopularProductsInDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate, 
                                                        org.springframework.data.domain.Pageable pageable) {
        return orderDetailRepository.findPopularProductsInDateRange(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getFrequentlyBoughtTogether(Long productId, org.springframework.data.domain.Pageable pageable) {
        return orderDetailRepository.findFrequentlyBoughtTogether(productId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByCategory(Long categoryId) {
        return orderDetailRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDetailDTO convertToDTO(OrderDetail orderDetail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(orderDetail.getId());
        dto.setAmount(orderDetail.getAmount());
        dto.setUnitPrice(orderDetail.getUnitPrice());
        
        if (orderDetail.getOrder() != null) {
            dto.setOrderId(orderDetail.getOrder().getId());
        }
        
        if (orderDetail.getProduct() != null) {
            dto.setProductId(orderDetail.getProduct().getId());
        }
        
        return dto;
    }

    private OrderDetail convertToEntity(OrderDetailDTO dto) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setAmount(dto.getAmount());
        orderDetail.setUnitPrice(dto.getUnitPrice());
        
        if (dto.getOrderId() != null) {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            orderDetail.setOrder(order);
        }
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderDetail.setProduct(product);
        }
        
        return orderDetail;
    }
}
