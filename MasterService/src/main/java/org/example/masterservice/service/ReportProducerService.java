package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.entity.Report;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportProducerService {
    private final KafkaTemplate<String, Report> kafkaTemplate;

    public void sendKafkaMessage(Report report, String topic) {
        try {
            kafkaTemplate.send(topic, report.getUuid().toString(), report).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("Kafka message send to topic - " + topic + " with report - " + report);
    }
}
