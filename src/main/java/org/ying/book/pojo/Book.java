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

    private Integer publishedYear;

    private String isbn;

    private Boolean available;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private List<File> files;

    private String description;

    private Library library;
}