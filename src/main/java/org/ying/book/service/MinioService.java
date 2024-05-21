package org.ying.book.service;

import io.micrometer.common.util.StringUtils;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ying.book.dto.file.FileDto;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioService {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

//    @Resource
    private MinioClient minioClient;

    @PostConstruct
    public void init() throws MinioException {
        minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public FileDto uploadFile(String bucketName, MultipartFile file) throws Exception {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)){
            throw new RuntimeException();
        }
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM/dd");
        String formattedDate = date.format(formatter);
        String objectName = formattedDate + "/" + fileName;
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).contentType(file.getContentType()).object(objectName).stream(file.getInputStream(),file.getSize(),-1).contentType(file.getContentType()).build());

        return FileDto.builder().objectName(objectName).contentType(file.getContentType()).build();
    }

    public InputStream getFile(String bucketName, String objectName) throws Exception {
        try{
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        }catch (Exception e){
            throw new RuntimeException("文件获取失败");
        }
    }

    public String getPresetSignedUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(1, TimeUnit.DAYS)  // URL will be valid for 60 minutes
                            .build());
        } catch (Exception e) {
//            throw new RuntimeException("Error while generating presigned URL", e);
            log.info("Error while generating presigned URL: " + e);
        }
        return null;
    }
}