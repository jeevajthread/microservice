package com.jthread.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jthread.productservice.entity.Product;
import com.jthread.productservice.exception.ProductNotFoundException;
import com.jthread.productservice.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        Product product= productRepository.findById(id).
        		orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
        //System.out.println(product);
        return product;
    }
}

