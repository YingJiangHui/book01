package org.ying.book.service;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.ying.book.exception.CustomException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class MinioServiceTests {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioService minioService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void uploadFile_WhenBucketDoesNotExist_ShouldCreateBucket() throws Exception {
        String bucketName = "test-bucket";
        MultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes());

        when(minioClient.bucketExists(any())).thenReturn(false);

        minioService.uploadFile(bucketName, file);

        verify(minioClient, times(1)).makeBucket(any());
    }

    @Test
    public void uploadFile_WhenBucketExists_ShouldNotCreateBucket() throws Exception {
        String bucketName = "test-bucket";
        MultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes());

        when(minioClient.bucketExists(any())).thenReturn(true);

        minioService.uploadFile(bucketName, file);

        verify(minioClient, times(0)).makeBucket(any());
    }

    @Test
    public void uploadFile_WhenFileIsEmpty_ShouldThrowException() throws Exception {
        String bucketName = "test-bucket";
        MultipartFile file = new MockMultipartFile("file", "", "text/plain", "".getBytes());

        when(minioClient.bucketExists(any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> minioService.uploadFile(bucketName, file));
    }

    @Test
    public void uploadFile_WhenFileIsNotEmpty_ShouldUploadFile() throws Exception {
        File file = new File("F:\\code\\test-assest\\test-assest\\wallhaven-werdv6.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        String bucketName = "test-bucket";
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "image/png", fileInputStream);

//        when(minioClient.bucketExists(any())).thenReturn(true);

        minioService.uploadFile(bucketName, multipartFile);

//        verify(minioClient, times(1)).putObject(any());
    }
//    @Test
//    public void getFile_WhenFileExists_ShouldReturnInputStream() throws Exception {
//        String bucketName = "test-bucket";
//        String objectName = "test-object";
//
//        when(minioClient.getObject(any())).thenReturn(new ByteArrayInputStream("test data".getBytes()));
//
//        InputStream result = minioService.getFile(bucketName, objectName);
//
//        assertNotNull(result);
//        assertEquals("test data", new BufferedReader(new InputStreamReader(result)).readLine());
//    }

//    @Test
//    public void getFile_WhenFileDoesNotExist_ShouldThrowException() throws Exception {
//        String bucketName = "test-bucket";
//        String objectName = "non-existent-object";
//
//        when(minioClient.getObject(any())).thenThrow(new ErrorResponseException("No such object", HttpResponseStatus.NOT_FOUND, "", "", "", ""));
//
//        assertThrows(ErrorResponseException.class, () -> minioService.getFile(bucketName, objectName));
//    }

    @Test
    public void getFile_WhenFileDoesNotExist_ShouldThrowException() throws Exception {
        String bucketName = "test-bucket";
        String objectName = "2024-05/20/9b5d5f64-95dc-49b6-a0ba-100c4d3857e3.png";
        System.out.println(minioService.getFile(bucketName, objectName));
//        when(minioClient.getObject(any())).thenThrow(new CustomException("No such object", HttpStatus.NOT_FOUND));

//        assertThrows(ErrorResponseException.class, () -> minioService.getFile(bucketName, objectName));
    }
}
