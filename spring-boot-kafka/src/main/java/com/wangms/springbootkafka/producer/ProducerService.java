package com.wangms.springbootkafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    /**
     * 异步发送
     */
    public Map<Boolean, String> sendMessageAnsyc(final String topic, final String message) {
        Map<Boolean, String> map = new HashMap<>();
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("消息发送成功：" + result);
                map.put(true, "消息发送成功");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("消息发送失败：" + ex.getMessage());
                map.put(false, "消息发送失败");
            }
        });
        return map;
    }
}
