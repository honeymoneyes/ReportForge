package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.entity.Report;
import org.example.workerservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    @KafkaListener(topics = "worker_1", groupId = "consumer-group-1")
    public void getPendingReportsFromMaster(Report report) {
        log.info("Worker-Service method worked - accept Kafka message");
        reportRepository.save(report);
    }
}
