package com.jthread.productservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.jthread.productservice.dto.OrderEvent;
import com.jthread.productservice.entity.Product;
import com.jthread.productservice.repository.ProductRepository;

@Service
public class KafkaConsumerService {

	@Autowired
	private ProductRepository productRepository;
	
	@KafkaListener(
	        topics = "order-topic",
	        groupId = "product-group"
	)
	public void consume(OrderEvent event) {
		
		

	    try {

	        System.out.println("Received Event: " + event);

	        Product product = productRepository
	                .findById(event.getProductId())
	                .orElseThrow(() ->
	                        new RuntimeException(
	                                "Product not found"));

	        if (product.getQuantity()
	                < event.getQuantity()) {

	            throw new RuntimeException(
	                    "Insufficient stock");
	        }

	        product.setQuantity(
	                product.getQuantity()
	                        - event.getQuantity());

	        productRepository.save(product);

	        System.out.println(
	                "Stock updated successfully");

	    } catch (Exception ex) {

	        System.out.println(
	                "Error while processing event: "
	                        + ex.getMessage());
	    }
	}
}