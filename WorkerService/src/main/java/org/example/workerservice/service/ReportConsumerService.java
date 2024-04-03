package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportConsumerService {
    private final ReportRepository reportRepository;

    @KafkaListener(topics = KafkaTopics.WORKER_1, groupId = ConsumerGroup.CONSUMER_GROUP_1)
    public void getPendingReportsFromMaster(ConsumerRecord<String, Report> report) {

        log.info("| Worker-Service | Consumer method worked | Accepted Kafka message |");

        List<Report> deduplicateReportIfExist =
                reportRepository.findAllByPhoneNumberAndStartDateAndEndDate(
                        report.value().getPhoneNumber(),
                        report.value().getStartDate(),
                        report.value().getEndDate());

        log.info("Checking for duplicate messages");

        if (deduplicateReportIfExist.isEmpty()) {
            reportRepository.save(report.value());
            log.info("Report saved.");
        } else {
            log.info("Duplicate message accepted");
            log.info("Message not saved");
            log.info("Report already exist");
        }
    }
}
