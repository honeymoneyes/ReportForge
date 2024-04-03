package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ConsumerGroup;
import org.example.masterservice.enums.KafkaTopics;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ReportConsumerService {
    private final ReportRepository reportRepository;

    @KafkaListener(topics = KafkaTopics.MASTER, groupId = ConsumerGroup.CONSUMER_GROUP_2)
    public void getPendingReportsFromMaster(ConsumerRecord<String, Report> report) {

        log.info("| Master | Method worked | Accepted Kafka message |");

        List<Report> duplicateReportIfExist =
                reportRepository.findAllByPhoneNumberAndStartDateAndEndDate(
                        report.value().getPhoneNumber(),
                        report.value().getStartDate(),
                        report.value().getEndDate());

        log.info("Checking for duplicate messages");

        if (duplicateReportIfExist.isEmpty()) {
            reportRepository.save(report.value());
            log.info("Report saved.");
        } else {
            log.info("Duplicate message accepted");
            log.info("Message not saved");
            log.info("Report already exist");
        }
    }
}
