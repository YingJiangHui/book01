package org.ying.book.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class LibraryUser {
    private Integer id;

    private Integer libraryId;

    private Integer userId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}