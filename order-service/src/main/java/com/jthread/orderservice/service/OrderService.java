package com.jthread.orderservice.service;

import java.util.List;

import com.jthread.orderservice.entity.Order;

import java.util.List;

public interface OrderService {

      public List<Order> getAllOrders();
      public Order placeOrder(Order order);
}
