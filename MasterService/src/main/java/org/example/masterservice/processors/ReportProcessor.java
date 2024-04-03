package org.example.masterservice.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.enums.KafkaTopics;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.example.masterservice.service.ReportProducerService;
import org.example.masterservice.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReportProcessor {

    private final ReportRepository reportRepository;
    private final ReportProducerService producerService;
    private final ReportService reportService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional(transactionManager = "transactionManager")
    @Async
    public void sendMessageToWorker() {
        log.error("Executing the @Scheduled method | Worker-Service");
        var allReportByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);

        if (!allReportByStatus.isEmpty()) {
            allReportByStatus.forEach(report -> {
                producerService.sendKafkaMessage(report, KafkaTopics.WORKER_1);
                reportService.changeReportStatus(report, ReportStatus.IN_PROGRESS);
                reportService.saveToReportDatabase(report);
            });
        }
    }
}
