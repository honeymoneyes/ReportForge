package org.example.masterservice.processors;

import lombok.RequiredArgsConstructor;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.Status;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class ReportProcessor {

    private final ReportRepository reportRepository;

    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    private void sendMessageToWorker() {
        var allReportByStatus = reportRepository.findAllByStatus(Status.PENDING);

        allReportByStatus.forEach(report -> {
            kafkaTemplate.send("worker1", report.getPhoneNumber(), report);
            report.setStatus(Status.IN_PROGRESS);
            reportRepository.save(report);
        });
    }
}
