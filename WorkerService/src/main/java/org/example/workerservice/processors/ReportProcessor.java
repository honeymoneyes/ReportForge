package org.example.workerservice.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.dto.FileDto;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.KafkaTopics;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.service.ApiService;
import org.example.workerservice.service.MinioService;
import org.example.workerservice.service.ReportProducerService;
import org.example.workerservice.service.ReportService;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportProcessor {

    private final ReportService reportService;
    private final MinioService minioService;
    private final ReportRepository reportRepository;
    private final ApiService apiService;
    private final ReportProducerService reportProducerService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional(transactionManager = "transactionManager")
    public void createReport() {
        log.error("Executing the @Scheduled method | Worker-Service");

        var allReportsByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);

        log.info(allReportsByStatus.size() + " - number of reports with PENDING status");

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
                    var file = convertReportToFile(readyReport);
                    FileDto fileDto = uploadFileToMinioAndGetDTO(report, file);
                    markAsDoneAndUpdateInDatabase(report, fileDto);
                } catch (IOException e) {
                    log.error("Error uploading file to Minio server", e);
                    throw new RuntimeException(e);
                }
                reportProducerService.sendKafkaMessage(report, KafkaTopics.MASTER);
            });
        }
    }

    private void markAsDoneAndUpdateInDatabase(Report report, FileDto fileDto) {
        reportService.changeReference(report, fileDto);
        reportService.changeReportStatus(report, ReportStatus.DONE);
        reportService.saveToReportDatabase(report);
    }

    @NotNull
    private static ByteArrayInputStream convertReportToFile(List<ClientResponse> readyReport) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var string = mapper.writeValueAsString(readyReport);
        var bytes = string.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    private FileDto uploadFileToMinioAndGetDTO(Report report, ByteArrayInputStream byteArrayInputStream) throws IOException {
        return minioService.uploadFile(FileDto.builder()
                .title(report.getPhoneNumber() + "|" + report.getStartDate() + "|" + report.getEndDate())
                .filename(report.getUuid().toString())
                .description("REPORT")
                .file(byteArrayInputStream)
                .size((long) byteArrayInputStream.available())
                .url(report.getUuid().toString())
                .build());
    }
}
