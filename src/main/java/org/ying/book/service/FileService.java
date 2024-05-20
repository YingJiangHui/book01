package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ying.book.dto.file.FileDto;
import org.ying.book.mapper.FileMapper;
import org.ying.book.pojo.File;

import java.io.InputStream;

@Service
public class FileService {
    @Resource
    MinioService minioService;

    @Value("file")
    private String bucketName;

    @Resource
    private FileMapper fileMapper;

    @Transactional
    public File uploadFile(MultipartFile multipartFile) throws Exception {
        FileDto fileDto = minioService.uploadFile(bucketName, multipartFile);
        File file = File.builder()
                .url(fileDto.getUrl())
                .contentType(fileDto.getContentType())
                .build();
        fileMapper.insertSelective(file);
        return file;
    }
    public InputStream getFile(String fileName) throws Exception {
        return minioService.getFile(bucketName, fileName);
    }

    public String getFileUrl(String fileName){
        return minioService.getPresetSignedUrl(bucketName, fileName);
    }
}
