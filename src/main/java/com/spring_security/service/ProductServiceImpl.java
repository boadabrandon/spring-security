package com.spring_security.service;

import com.spring_security.entity.Product;
import com.spring_security.exception.ProductNotFoundException;
import com.spring_security.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
            var products = productRepository.findAll();

            if ( products.isEmpty()) {
                throw new ProductNotFoundException("No product found");
            }
            return products;
    }

    @Override
    public Product createOne(Product product) {
        return productRepository.save(product);
    }
}

