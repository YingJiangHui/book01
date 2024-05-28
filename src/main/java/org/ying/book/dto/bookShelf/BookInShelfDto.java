package org.ying.book.dto.bookShelf;

import lombok.Data;
import org.ying.book.pojo.Book;

@Data
public class BookInShelfDto extends Book {
    private Integer bookId;
}
