package org.example.masterservice.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportProcessor {

    private final ReportRepository reportRepository;

    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional(transactionManager = "transactionManager")
    public void sendMessageToWorker() {
        log.info("The method is executed according to schedule in the Master Service");
        var allReportByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);

        if (!allReportByStatus.isEmpty()) {
            allReportByStatus.forEach(report -> {
                sendKafkaMessage(report);
                changeReportStatus(report);
                saveToDB(report);
            });
        }
    }

    private static void changeReportStatus(Report report) {
        report.setReportStatus(ReportStatus.IN_PROGRESS);
        log.info("Set Report Status");
    }

    public void saveToDB(Report report) {
        reportRepository.save(report);
        log.info("Save report to database");
        throw new RuntimeException("DB_EXCEPTION");
    }

    public void sendKafkaMessage(Report report) {
        kafkaTemplate
                .send("worker_1",
                        getUniqueKey(
                                report.getPhoneNumber(),
                                report.getStartDate(),
                                report.getEndDate()), report);
        log.info("Kafka message send");
    }

    public static String getUniqueKey(String phoneNumber, Date startDate, Date endDate) {
        return UUID.nameUUIDFromBytes((phoneNumber +
                        startDate.toString() +
                        endDate.toString())
                        .getBytes())
                .toString();
    }
}
