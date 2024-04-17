package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    public void changeReportStatus(Report report, ReportStatus status) {
        report.setReportStatus(status);
        log.info("Set report status - " + status.name() + " for report - " + report);
    }

    public void saveToReportDatabase(Report report) {
        reportRepository.save(report);
        log.error("Hashcode - " + report.hashCode());
        log.info("Save report  - " + report + " - to database");
    }
}
