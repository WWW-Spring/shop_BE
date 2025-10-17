package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ProductDTO;
import com.iuh.fit.se.domain.enums.ProductStatus;
import com.iuh.fit.se.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {


}
