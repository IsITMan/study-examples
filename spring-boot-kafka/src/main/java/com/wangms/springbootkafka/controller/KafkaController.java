package com.wangms.springbootkafka.controller;

import com.wangms.springbootkafka.producer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private ProducerService producerService;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage("test", message);
        return "success";
    }


    @GetMapping("/sendMessageAnsyc")
    public Map<Boolean, String> sendMessageAnsyc(@RequestParam String message) {
        Map<Boolean, String> map = producerService.sendMessageAnsyc("test", message);
        return map;
    }
}
