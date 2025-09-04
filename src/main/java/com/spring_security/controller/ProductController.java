package com.spring_security.controller;

import com.spring_security.entity.Product;
import com.spring_security.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PreAuthorize("hasAuthority('SAVE_ONE_PRODUCT')")
    @PostMapping
    public ResponseEntity<Product> createOne(@RequestBody @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createOne(product));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception exception, HttpServletRequest request){
        Map<String, String> apiError = new HashMap<>();
        apiError.put("http-method", request.getMethod());
        apiError.put("message", exception.getLocalizedMessage());
        apiError.put("url", request.getRequestURL().toString());
        apiError.put("timestamp", new Date().toString());

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if(exception instanceof AccessDeniedException) {
            httpStatus = HttpStatus.FORBIDDEN;
        }
        
        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
