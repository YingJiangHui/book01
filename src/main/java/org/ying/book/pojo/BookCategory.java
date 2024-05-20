package org.ying.book.pojo;

import jakarta.validation.Constraint;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCategory {
    private Integer id;

    private String categoryName;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}