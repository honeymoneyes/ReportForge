package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.entity.Report;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportProducerService {
    private final KafkaTemplate<String, Report> kafkaTemplate;

    public void sendKafkaMessage(Report report, String topic) {
        try {
            kafkaTemplate.send(topic, report.getUuid().toString(), report).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("Kafka message method finish");
    }
}
