package org.ying.book.pojo;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBook {
    private Integer id;

    private Integer libraryId;

    private Integer bookId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}