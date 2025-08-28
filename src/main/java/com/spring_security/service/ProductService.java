package com.spring_security.service;

import com.spring_security.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product createOne(Product product);
}
