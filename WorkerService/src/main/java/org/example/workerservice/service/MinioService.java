package org.example.workerservice.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workerservice.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    public FileDto uploadFile(FileDto request) throws IOException {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getFilename())
                    .stream(request.getFile(), request.getFile().available(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        // todo Проверить для чего используется и где
        return FileDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(getPreSignedUrl(request.getFilename()))
                .size(request.getSize())
                .filename(request.getFilename())
                .build();
    }

    public InputStream downloadFile(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }
        // todo написать логику использования
        return stream;
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

    private String getPreSignedUrl(String filename) {
        return "http://localhost:8081/file/".concat(filename);
    }
}