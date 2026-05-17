package com.jthread.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jthread.orderservice.dto.Product;

@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("/api/products/{id}")
	Product getProduct(@PathVariable String id);
}