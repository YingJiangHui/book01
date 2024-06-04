package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
public class SystemSetting {
    private Integer id;
    @Nullable
    private String name;

    private String value;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;
}