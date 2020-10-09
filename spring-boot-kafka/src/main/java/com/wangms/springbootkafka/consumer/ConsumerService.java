package com.wangms.springbootkafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class ConsumerService {

    @KafkaListener(topics="test")
    public void onMessage(String message){
        System.out.println(message);
    }
}
