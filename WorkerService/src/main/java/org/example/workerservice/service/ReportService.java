package org.example.workerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.dto.FileDto;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    public final ReportRepository reportRepository;

    public void changeReportStatus(Report report, ReportStatus status) {
        report.setReportStatus(status);
        log.info("Set report status - " + status.name() + " for report - " + report);
    }

    public void changeReference(Report report, FileDto fileDto) {
        report.setReference(fileDto.getUrl());
        log.info("Set reference - " + fileDto.getUrl() + " for report - " + report);
    }

    public void saveToReportDatabase(Report report) {
        reportRepository.save(report);
        log.info("Save report  - " + report + " - to database");
    }
}
