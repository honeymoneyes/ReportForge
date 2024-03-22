package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.dto.ReportResponse;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.Status;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportResponse handleReportDelivery(ReportDTO reportDTO) {

        var report = createReport(reportDTO);

        return ReportResponse.builder()
                .uuid(report.getUuid())
                .build();
    }

    private Report createReport(ReportDTO reportDTO) {
        UUID uuidReport = UUID.randomUUID();

        var report = Report.builder()
                .uuid(uuidReport)
                .phoneNumber(reportDTO.getPhoneNumber())
                .startDate(reportDTO.getStartDate())
                .endDate(reportDTO.getEndDate())
                .status(Status.PENDING)
                .build();

        reportRepository.save(report);

        return report;
    }
}
