package com.rsegador.workshop.testersservice.logs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class HttpCallLogPublisher {

    @Value(value = "${log.topic.name}")
    private String kafkaTopic;

    final KafkaTemplate<String, HttpCallLog> kafkaTemplate;

    public HttpCallLogPublisher(KafkaTemplate<String, HttpCallLog> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendLogMessage(HttpCallLog httpCallLog) {
        ListenableFuture<SendResult<String, HttpCallLog>> future = kafkaTemplate.send(kafkaTopic, httpCallLog);

        future.addCallback(new ListenableFutureCallback<SendResult<String, HttpCallLog>>() {

            @Override
            public void onSuccess(SendResult<String, HttpCallLog> result) {
                System.out.println("Sent message=[" + httpCallLog.toString() + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "] to topic [" + result.getRecordMetadata().topic() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + httpCallLog.toString() + "] due to : " + ex.getMessage());
            }
        });
    }
}
