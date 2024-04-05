package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.dto.ReferenceResponse;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReferenceResponseStatus;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReportHandler {
    private final ReportRepository reportRepository;

    @Transactional(transactionManager = "transactionManager")
    public ReferenceResponse handleReportDelivery(ReportDTO reportDTO) {

        List<Report> reportIfExist = getReportIfExist(reportDTO);

        if (reportIfExist.isEmpty()) {
            Report report = createReport(reportDTO);
            log.info("The report is created");
            log.error("Hashcode - " + report.hashCode());
            return getReferenceResponse(ReferenceResponseStatus.REFERENCE_DESCRIPTION_IF_REPORT_IS_NOT_READY_YET, report);
        } else {
            Report report = reportIfExist.get(0);
            log.info("Report already exist.");
            return getReferenceResponse(ReferenceResponseStatus.REFERENCE_DESCRIPTION_IF_REPORT_IS_READY_YET, report);
        }
    }

    private static ReferenceResponse getReferenceResponse(String description, Report report) {
        return ReferenceResponse.builder()
                .description(description)
                .reference(ReferenceResponseStatus.REFERENCE_FOR_REPORT.concat(report.getUuid().toString()))
                .build();
    }

    private List<Report> getReportIfExist(ReportDTO reportDTO) {
        return reportRepository.findAllByPhoneNumberAndStartDateAndEndDate(
                reportDTO.getPhoneNumber(),
                reportDTO.getStartDate(),
                reportDTO.getEndDate());
    }

    private Report createReport(ReportDTO reportDTO) {
        return reportRepository.save(Report.builder()
                .uuid(UUID.randomUUID())
                .phoneNumber(reportDTO.getPhoneNumber())
                .startDate(reportDTO.getStartDate())
                .endDate(reportDTO.getEndDate())
                .reportStatus(ReportStatus.PENDING)
                .build());
    }
}
