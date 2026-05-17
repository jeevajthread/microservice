package com.jthread.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jthread.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

