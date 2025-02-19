package com.handson.searchengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handson.searchengine.kafka.Producer;
import com.handson.searchengine.model.CrawlerRecord;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private final Producer producer;

    public KafkaService(Producer producer) {
        this.producer = producer;
    }

    public void sendMessage(CrawlerRecord record) throws JsonProcessingException {
        producer.send(record);
    }
}
