package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import org.example.masterservice.dto.ReferenceResponse;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public ReferenceResponse handleReportDelivery(ReportDTO reportDTO) {

        var report = createReport(reportDTO);

        return ReferenceResponse.builder()
                .reference("http://localhost:8081/file/".concat(report.getUuid().toString()))
                .build();
    }

    private Report createReport(ReportDTO reportDTO) {
        UUID uuidReport = UUID.randomUUID();

        var report = Report.builder()
                .uuid(uuidReport)
                .phoneNumber(reportDTO.getPhoneNumber())
                .startDate(reportDTO.getStartDate())
                .endDate(reportDTO.getEndDate())
                .reportStatus(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);

        return report;
    }

    @KafkaListener(topics = "master", groupId = "consumer-group-2")
    public void getPendingReportsFromMaster(Report report) {
        System.out.println("Сработал метод Master-Service - принять сообщение Kafka");
        reportRepository.save(report);
    }
}
