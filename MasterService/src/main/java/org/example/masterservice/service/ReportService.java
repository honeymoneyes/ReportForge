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

    public ReportResponse createReport(ReportDTO reportDTO) {
        UUID uuidReport = UUID.randomUUID();

        var report = Report.builder()
                .uuid(uuidReport)
                .firstname(reportDTO.getFirstname())
                .lastname(reportDTO.getLastname())
                .startDate(reportDTO.getStartDate())
                .endDate(reportDTO.getEndDate())
                .status(Status.PENDING)
                .build();

        reportRepository.save(report);

        return ReportResponse.builder()
                .uuid(uuidReport)
                .build();
    }
}
