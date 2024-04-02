package org.example.workerservice.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.dto.FileDto;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.service.ApiService;
import org.example.workerservice.service.MinioService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportProcessor {

    private final MinioService minioService;
    private final ReportRepository reportRepository;
    private final ApiService apiService;
    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional(transactionManager = "transactionManager")
    public void createReport() {
        log.info("Executing the @Scheduled method - worker-service");
        var allReportsByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);

        log.info(allReportsByStatus.size() + " - sheet size of the received request by PENDING status");

        if (!allReportsByStatus.isEmpty()) {
            // Перебирая все отчеты с статусом PENDING, вызываем метод api service ( READ ONLY DB )
            // и начинаем построение отчета. Ответ поступит в виде готового отчета, который требуется
            // сохранить в S3, а ссылку вернуть в report.
            allReportsByStatus.forEach(report -> {
                // Построение готового отчета
                List<ClientResponse> readyReport = apiService.getInformationByNumberAndDate(
                        report.getPhoneNumber(),
                        report.getStartDate(),
                        report.getEndDate());
                log.info(readyReport.size() + " - number of events in the report");

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    var string = mapper.writeValueAsString(readyReport);
                    var bytes = string.getBytes();
                    var byteArrayInputStream = new ByteArrayInputStream(bytes);
                    FileDto fileDto = minioService.uploadFile(FileDto.builder()
                            .title(report.getUuid().toString())
                            .filename(report.getUuid().toString())
                            .description("REPORT")
                            .file(byteArrayInputStream)
                            .size((long) byteArrayInputStream.available())
                            .url(report.getUuid().toString())
                            .build());

                    report.setReference(fileDto.getUrl());
                    report.setReportStatus(ReportStatus.DONE);
                    reportRepository.save(report);
                } catch (IOException e) {
                    log.error("In the catch block ReportProcessor - generating a report for loading into Minio");
                    throw new RuntimeException(e);
                }
                try {
                    kafkaTemplate.send("master",
                            getUniqueKey(
                                    report.getPhoneNumber(),
                                    report.getStartDate(),
                                    report.getEndDate()), report).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private String getUniqueKey(String phoneNumber, Date startDate, Date endDate) {
        return UUID.nameUUIDFromBytes((phoneNumber +
                        startDate.toString() +
                        endDate.toString())
                        .getBytes())
                .toString();
    }
}
