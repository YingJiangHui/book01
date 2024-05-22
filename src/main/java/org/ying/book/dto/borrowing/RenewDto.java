package org.ying.book.dto.borrowing;

import lombok.Data;

import java.util.List;

@Data
public class RenewDto {
    List<Integer> borrowingIds;
    Integer days;
}
