package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.ProductDTO;
import com.iuh.fit.se.domain.entity.Category;
import com.iuh.fit.se.domain.entity.Product;
import com.iuh.fit.se.domain.enums.ProductStatus;
import com.iuh.fit.se.repository.CategoryRepository;
import com.iuh.fit.se.repository.ProductRepository;
import com.iuh.fit.se.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {



}
