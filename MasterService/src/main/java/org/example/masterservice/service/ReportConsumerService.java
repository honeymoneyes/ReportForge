package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ConsumerGroup;
import org.example.masterservice.enums.KafkaTopics;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportConsumerService {
    private final ReportRepository reportRepository;

    @KafkaListener(topics = KafkaTopics.MASTER, groupId = ConsumerGroup.CONSUMER_GROUP_2)
    public void getPendingReportsFromWorker(Report report) {

        log.info("| Master | Method worked | Accepted Kafka message |");

        List<Report> duplicateReportIfExist =
                reportRepository.findAllByPhoneNumberAndStartDateAndEndDate(
                        report.getPhoneNumber(),
                        report.getStartDate(),
                        report.getEndDate());

        log.info("Checking for duplicate messages");

        if (duplicateReportIfExist.get(0).getReference() == null) {
            reportRepository.save(report);
            log.error("Hashcode - " + report.hashCode());
            log.info("Report saved.");
        } else {
            log.info("Duplicate message accepted");
            log.info("Message not saved");
            log.info("Report already exist");
        }
    }
}
