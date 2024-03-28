package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.workerservice.entity.Report;
import org.example.workerservice.entity.UniqueMessage;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.repository.UniqueMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final UniqueMessageRepository uniqueMessageRepository;

    @KafkaListener(topics = "worker_1", groupId = "consumer-group-1")
    @Transactional
    public void getPendingReportsFromMaster(ConsumerRecord<String, Report> report) {
        log.info("Worker-Service method worked - accept Kafka message");

        Optional<UniqueMessage> uniqueMessageByUniqueKey = uniqueMessageRepository
                .findUniqueMessageByUniqueKey(report.key());

        if (uniqueMessageByUniqueKey.isEmpty()) {

            UniqueMessage uniqueMessage = UniqueMessage.builder()
                    .uniqueKey(report.key())
                    .build();

            uniqueMessageRepository.save(uniqueMessage);
            log.info("UniqueMessage saved");

            reportRepository.save(report.value());
            log.info("Report saved.");

        }
        log.info("Report already exist.");
    }
}
