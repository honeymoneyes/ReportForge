package org.example.workerservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.dto.FileDto;
import org.example.workerservice.enums.References;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    public FileDto uploadFile(FileDto request) {
        try {
            log.info("In the try MinioService upload file block");
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getFilename())
                    .stream(request.getFile(), request.getFile().available(), -1)
                    .build());

            log.info("Error did not occur when saving");
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return FileDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(getPreSignedUrl(request.getFilename()))
                .size(request.getSize())
                .filename(request.getFilename())
                .build();
    }

    private String getPreSignedUrl(String url) {
        return References.REFERENCE_FOR_REPORT.concat(url);
    }
}