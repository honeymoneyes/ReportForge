package org.example.masterservice.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportProcessor {

    private final ReportRepository reportRepository;

    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Async
    public void sendMessageToWorker() {
        log.info("The method is executed according to schedule in the Master Service");
        var allReportByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);
        if (!allReportByStatus.isEmpty()) {
            allReportByStatus.forEach(report -> {
                kafkaTemplate.send("worker_1", report.getPhoneNumber(), report);
                report.setReportStatus(ReportStatus.IN_PROGRESS);
                reportRepository.save(report);
            });
        }
    }
}
