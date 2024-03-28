package org.example.masterservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.masterservice.dto.ReferenceResponse;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.entity.Report;
import org.example.masterservice.entity.UniqueMessage;
import org.example.masterservice.enums.ReportStatus;
import org.example.masterservice.processors.ReportProcessor;
import org.example.masterservice.repository.ReportRepository;
import org.example.masterservice.repository.UniqueMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final UniqueMessageRepository uniqueMessageRepository;

    @Transactional
    public ReferenceResponse handleReportDelivery(ReportDTO reportDTO) {
        String uniqueKey = ReportProcessor.getUniqueKey(
                reportDTO.getPhoneNumber(),
                reportDTO.getStartDate(),
                reportDTO.getEndDate());

        Optional<UniqueMessage> uniqueMessageByUniqueKey = uniqueMessageRepository
                .findUniqueMessageByUniqueKey(uniqueKey);

        if (uniqueMessageByUniqueKey.isEmpty()) {
            createReport(reportDTO, uniqueKey);

            return ReferenceResponse.builder()
                    .description("Your report will be ready at this link after some time")
                    .reference("http://localhost:8080/worker-service/file/".concat(uniqueKey))
                    .build();
        }
        log.info("Report already exist.");
        return null;
    }

    public void createReport(ReportDTO reportDTO, String uniqueKey) {

        var report = Report.builder()
                .uuid(uniqueKey)
                .phoneNumber(reportDTO.getPhoneNumber())
                .startDate(reportDTO.getStartDate())
                .endDate(reportDTO.getEndDate())
                .reportStatus(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }

    @KafkaListener(topics = "master", groupId = "consumer-group-2")
    @Transactional
    public void getPendingReportsFromMaster(ConsumerRecord<String, Report> report) {
        log.info("Master-Service method worked - accept Kafka message");

        Optional<UniqueMessage> uniqueMessageByUniqueKey = uniqueMessageRepository
                .findUniqueMessageByUniqueKey(report.key());

        if (uniqueMessageByUniqueKey.isEmpty()) {

            UniqueMessage uniqueMessage = UniqueMessage.builder()
                    .uniqueKey(report.key())
                    .build();

            uniqueMessageRepository.save(uniqueMessage);
            log.info("UniqueMessage saved");

            reportRepository.save(report.value());
            log.info("Report saved.");
        }
        log.info("Report already exist.");
    }
}
