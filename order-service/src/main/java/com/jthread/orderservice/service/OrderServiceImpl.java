package com.jthread.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jthread.orderservice.client.ProductClient;
import com.jthread.orderservice.dto.OrderEvent;
import com.jthread.orderservice.dto.Product;
import com.jthread.orderservice.entity.Order;
import com.jthread.orderservice.exception.ProductNotFoundException;
import com.jthread.orderservice.repository.OrderRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private KafkaProducerService producer;

	public Order placeOrder(Order order) {

		order.setOrderDate(LocalDateTime.now());  
	    Order savedOrder = orderRepository.save(order);

	    OrderEvent event = new OrderEvent();
	    event.setProductId(order.getProductId());
	    event.setQuantity(order.getQuantity());

	    producer.sendOrderEvent(event);

	    return savedOrder;
	}

	/*
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
	public Order placeOrder(Order order) {
		Product product;

		try {
			product = productClient.getProduct(order.getProductId());
		} catch (FeignException.NotFound e) {
			System.out.println("Exception0: " + e.getClass().getName());
			System.out.println("Exception0: " + e.getMessage());

			throw new ProductNotFoundException("Product not found with id " + order.getProductId());
		} catch (FeignException e) {
			System.out.println("Exception1: " + e.getClass().getName());
			System.out.println("Exception1: " + e.getMessage());

			if (e.status() == 503) {
				throw new RuntimeException("Product Service is down. Please try again later.");
			}

			throw new RuntimeException("Product Service error: " + e.status());
		}
		
		if (product.getQuantity() < order.getQuantity()) {
            throw new RuntimeException("Insufficient Stock");
        }


		order.setOrderDate(LocalDateTime.now());
		return orderRepository.save(order);
	}
    */
    public Order fallbackProduct(Order order, Throwable ex) {

        if (ex instanceof ProductNotFoundException) {
            throw (ProductNotFoundException) ex;
        }

        if (ex instanceof RuntimeException &&
            "Insufficient Stock".equals(ex.getMessage())) {
            throw (RuntimeException) ex;
        }

        throw new RuntimeException(
            "Fallback triggered: Product Service is temporarily unavailable."
        );
    }


	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
}