package org.ying.book.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private List<MultipartFile> file;
    private Integer id;

    private String title;

    private String author;

    private Integer categoryId;

    private Integer publishedYear;

    private String isbn;

    private Boolean available;

    private String description;

    private Integer libraryId;
}
