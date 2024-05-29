package org.ying.book.pojo;

import lombok.*;
import org.ying.book.dto.file.FileDto;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;

    private String title;

    private String author;

    private Integer categoryId;

    private String publishedYear;

    private String isbn;

    private Boolean available;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;


    private String publisher;

    private String description;

    private Library library;

    private List<File> files;
}