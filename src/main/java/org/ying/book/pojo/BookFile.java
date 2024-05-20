package org.ying.book.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFile {
    private Integer id;

    private Long bookId;

    private Long fileId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;
}