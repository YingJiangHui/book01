package org.ying.book.dto.bookShelf;


import lombok.Data;
import org.ying.book.pojo.Library;

import java.util.List;

@Data
public class BookShelfGroupByLibraryDto extends Library {
    List<BookInShelfDto> books;
}
