package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.bookShelf.BookInShelfDto;
import org.ying.book.dto.bookShelf.BookShelfGroupByLibraryDto;
import org.ying.book.mapper.BookShelfMapper;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.BookShelf;
import org.ying.book.pojo.BookShelfExample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookShelfService {

    @Resource
    private BookShelfMapper bookShelfMapper;

    @Resource
    BookService bookService;
    private List<BookShelf> getBookShelfByUserIdAndBookIds(Integer userId, List<Integer> bookIds) {
        BookShelfExample bookShelfExample = new BookShelfExample();
        bookShelfExample.createCriteria().andUserIdEqualTo(userId).andBookIdIn(bookIds).andDeletedEqualTo(false);
        List<BookShelf> bookShelves = bookShelfMapper.selectByExample(bookShelfExample);

        return bookShelves;
    }

    private BookShelf getBookShelfByUserIdAndBookId(Integer userId, Integer bookId) {
        return this.getBookShelfByUserIdAndBookIds(userId, List.of(bookId)).get(0);
    }

    @Transactional
    public BookShelf addBookToShelf(BookShelf bookShelf) {
        BookShelf bookShelfInTable = this.getBookShelfByUserIdAndBookId(bookShelf.getUserId(), bookShelf.getBookId());
        if (bookShelfInTable != null) {
            throw new RuntimeException("书架已存在该书，请勿重复添加");
        }
        bookShelfMapper.insertSelective(bookShelf);
        return bookShelf;
    }

    public List<BookShelfGroupByLibraryDto> getBookShelfByUserId(Integer userId) {
        BookShelfExample bookShelfExample = new BookShelfExample();
        bookShelfExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        Map<Integer,BookShelfGroupByLibraryDto> hashMap = new HashMap<>();

        bookShelfMapper.selectByExample(bookShelfExample).stream().map((bookShelf)->{
            Book book = bookService.getBook(bookShelf.getBookId());
            BookInShelfDto bookInShelfDto = (BookInShelfDto) book;
            bookInShelfDto.setBookId(bookShelf.getBookId());
            bookInShelfDto.setId(bookShelf.getId());
            return bookInShelfDto;
        }).forEach((bookInShelfDto)->{
            BookShelfGroupByLibraryDto bookShelfGroupByLibraryDto = (BookShelfGroupByLibraryDto)bookInShelfDto.getLibrary();
            List<BookInShelfDto> books = bookShelfGroupByLibraryDto.getBooks();
            if(books != null){
                books.add(bookInShelfDto);
            }else{
                bookShelfGroupByLibraryDto.setBooks(List.of(bookInShelfDto));
                hashMap.put(bookInShelfDto.getLibrary().getId(), bookShelfGroupByLibraryDto);
            }
        });

       return hashMap.entrySet().stream().map((item)-> item.getValue()).toList();
    }

    @Transactional
    public void removeBookFromShelf(Integer bookShelfId) {
        BookShelf bookShelfInTable = bookShelfMapper.selectByPrimaryKey(bookShelfId);
        if(bookShelfInTable == null){
            throw new RuntimeException("书架不存在该书，请检查");
        }
        bookShelfInTable.setDeleted(true);
        bookShelfMapper.updateByPrimaryKey(bookShelfInTable);
    }


    @Transactional
    public List<BookShelf> removeBooksFromShelf(List<Integer> bookShelfIds) {

        BookShelfExample bookShelfExample = new BookShelfExample();
        bookShelfExample.createCriteria().andIdIn(bookShelfIds).andDeletedEqualTo(false);

        List<BookShelf> bookShelves = bookShelfMapper.selectByExample(bookShelfExample);
        if(bookShelves.size() != bookShelfIds.size()){
            throw new RuntimeException("存在图书不在书架中");
        }
        bookShelves.forEach((bookShelf) -> {
            bookShelf.setDeleted(true);
            bookShelfMapper.updateByPrimaryKey(bookShelf);
        });
        return bookShelves;
    }

    @Transactional
    public List<BookShelf> removeBooksFromShelfByUserIdAndBookIds(Integer userId, List<Integer> bookIds) {
        List<BookShelf> bookShelves = this.getBookShelfByUserIdAndBookIds(userId, bookIds);
//        if(bookShelves.size() != bookIds.size()){
//            throw new RuntimeException("存在图书不在书架中");
//        }
        bookShelves.forEach((bookShelf) -> {
            bookShelf.setDeleted(true);
            bookShelfMapper.updateByPrimaryKey(bookShelf);
        });
        return bookShelves;
    }

}
