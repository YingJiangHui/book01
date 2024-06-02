package org.ying.book.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Search {
    private Integer id;
    private Integer userId;
    private String keyword;
    private Integer count;
    private Date createdAt;
    private Date updatedAt;
    private Boolean deleted;

    private String target;

}