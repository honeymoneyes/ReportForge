package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ConsumerGroup;
import org.example.workerservice.enums.KafkaTopics;
import org.example.workerservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportConsumerService {
    private final ReportRepository reportRepository;

    @KafkaListener(topics = KafkaTopics.WORKER_1, groupId = ConsumerGroup.CONSUMER_GROUP_1)
    @Transactional(transactionManager = "transactionManager")
    public void getPendingReportsFromMaster(Report report) {

        log.info("| Worker-Service | Consumer method worked | Accepted Kafka message |");

        List<Report> deduplicateReportIfExist =
                reportRepository.findAllByPhoneNumberAndStartDateAndEndDate(
                        report.getPhoneNumber(),
                        report.getStartDate(),
                        report.getEndDate());

        log.info("Checking for duplicate messages");

        if (deduplicateReportIfExist.isEmpty()) {
            reportRepository.save(report);
            log.error("Hashcode - " + report.hashCode());
            log.info("Report saved.");
//            throw new RuntimeException("EXCEPTION");
        } else {
            log.info("Duplicate message accepted");
            log.info("Message not saved");
            log.info("Report already exist");
        }
    }
}
