package org.ying.book.pojo;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private String url;

    private Integer id;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private String contentType;

    private String bucketName;

    private String objectName;
}