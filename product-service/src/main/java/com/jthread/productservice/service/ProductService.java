package com.jthread.productservice.service;

import java.util.List;

import com.jthread.productservice.entity.Product;

public interface ProductService {

    public List<Product> getAll();

    public Product save(Product product);
    
    public Product getById(Long id);
}
