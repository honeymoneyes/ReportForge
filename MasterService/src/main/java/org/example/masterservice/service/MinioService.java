package org.example.masterservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.masterservice.dto.ClientResponse;
import org.example.masterservice.dto.FileDto;
import org.example.masterservice.enums.References;
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

    public List<ClientResponse> downloadFile(String filename) {
        GetObjectResponse stream;
        List<ClientResponse> clientResponse = null;
        int maxAttempts = 3;
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                log.info("In the try MinioService download file block");
                stream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .build());

                byte[] bytes = stream.readAllBytes();
                ObjectMapper mapper = new ObjectMapper();
                clientResponse = mapper.readValue(bytes, new TypeReference<>() {
                });

                // Если файл загружен успешно, выходим из цикла
                break;

            } catch (Exception e) {
                log.error("Error occurred while trying to download file from Minio: ", e);
                attempts++;

                if (attempts < maxAttempts) {
                    log.info("Retrying in 5 seconds...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        if (clientResponse == null) {
            log.error("File '{}' not found or does not exist.", filename);
        }

        return clientResponse;
    }

    public List<FileDto> getListObjects() {
        List<FileDto> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(FileDto.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return objects;
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
        }

        return objects;
    }

    private String getPreSignedUrl(String url) {
        return References.REFERENCE_FOR_REPORT.concat(url);
    }
}
