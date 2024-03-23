package org.example.workerservice.processors;

import lombok.RequiredArgsConstructor;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportProcessor {

    private final ReportRepository reportRepository;

    @Scheduled(fixedRate = 5000)
    private void createReport() {
        var allReportByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);

        allReportByStatus.forEach(report -> {

        });
    }
}
