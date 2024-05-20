package org.ying.book.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private Integer id;

    private String url;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private String contentType;
}