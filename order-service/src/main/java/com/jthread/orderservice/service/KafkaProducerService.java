package com.jthread.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.jthread.orderservice.dto.OrderEvent;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event) {

        String key = event.getProductId().toString();

        kafkaTemplate.send(
                "order-topic",
                key,
                event
        ).thenAccept(result -> {

            System.out.println(
                    "Message sent to partition: "
                            + result.getRecordMetadata().partition()
            );

        });
    }
}