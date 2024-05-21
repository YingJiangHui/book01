package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ying.book.dto.book.BookDto;
import org.ying.book.pojo.Book;
import org.ying.book.service.BookService;
import org.ying.book.service.FileService;

import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Resource
    private BookService bookService;
    @Resource
    private FileService fileService;

    @GetMapping
    public String getBooks(@RequestParam("id") Integer id) throws Exception {
//        InputStream in = fileService.getFile("2024-05/20/9b5d5f64-95dc-49b6-a0ba-100c4d3857e3.png");
//        String contentTypeString = URLConnection.guessContentTypeFromStream(in);
//        if (contentTypeString == null) {
//            contentTypeString = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Use a default content type
//        }
//        MediaType contentType = MediaType.parseMediaType(contentTypeString);
//        return ResponseEntity.ok().contentType(contentType).body(in.readAllBytes());

        return fileService.getFileUrl("2024-05/21/c8994ae3-b3d4-4c2f-98af-802ec559d7b7.jpg");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book createBook(@ModelAttribute BookDto bookDto) throws Exception {
        return bookService.createBook(bookDto);
    }

}
