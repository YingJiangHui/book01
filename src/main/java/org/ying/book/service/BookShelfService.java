package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.bookShelf.BookInShelfDto;
import org.ying.book.dto.bookShelf.BookShelfGroupByLibraryDto;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BookShelfMapper;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.BookShelf;
import org.ying.book.pojo.BookShelfExample;

import java.util.*;

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
        List<BookShelf> bookShelves = this.getBookShelfByUserIdAndBookIds(userId, List.of(bookId));
        return bookShelves.isEmpty() ? null : bookShelves.get(0);
    }

    @Transactional
    public BookShelf addBookToShelf(BookShelf bookShelf) {
        BookShelf bookShelfInTable = this.getBookShelfByUserIdAndBookId(bookShelf.getUserId(), bookShelf.getBookId());
        if (bookShelfInTable != null) {
            throw new CustomException("图书已加入书架", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        bookShelfMapper.insertSelective(bookShelf);
        return bookShelf;
    }

    public List<BookShelfGroupByLibraryDto> getBookShelfByUserId(Integer userId) {
        BookShelfExample bookShelfExample = new BookShelfExample();
        bookShelfExample.setOrderByClause("id desc");
        bookShelfExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        Map<Integer, BookShelfGroupByLibraryDto> hashMap = new HashMap<>();

        bookShelfMapper.selectByExample(bookShelfExample).stream().map((bookShelf) -> {
            Book book = bookService.getBook(bookShelf.getBookId());
            BookInShelfDto bookInShelfDto = new BookInShelfDto();
            BeanUtils.copyProperties(book, bookInShelfDto);
            bookInShelfDto.setBookId(bookShelf.getBookId());
            bookInShelfDto.setId(bookShelf.getId());
            return bookInShelfDto;
        }).forEach((bookInShelfDto) -> {
//            对同一个图书馆的图书进行合并
            BookShelfGroupByLibraryDto bookShelfGroupByLibraryDto1 = hashMap.get(bookInShelfDto.getLibrary().getId());
            if (bookShelfGroupByLibraryDto1 != null) {
                List<BookInShelfDto> newList = new ArrayList<>(bookShelfGroupByLibraryDto1.getBooks());
                newList.add(bookInShelfDto);
                bookShelfGroupByLibraryDto1.setBooks(newList);
            } else {
                BookShelfGroupByLibraryDto bookShelfGroupByLibraryDto = new BookShelfGroupByLibraryDto();
                BeanUtils.copyProperties(bookInShelfDto.getLibrary(), bookShelfGroupByLibraryDto);
                bookShelfGroupByLibraryDto.setBooks(Arrays.asList(bookInShelfDto));
                hashMap.put(bookInShelfDto.getLibrary().getId(), bookShelfGroupByLibraryDto);
            }
        });

        return hashMap.entrySet().stream().map((item) -> item.getValue()).toList();
    }

    @Transactional
    public void removeBookFromShelf(Integer bookShelfId) {
        BookShelf bookShelfInTable = bookShelfMapper.selectByPrimaryKey(bookShelfId);
        if (bookShelfInTable == null) {
            throw new CustomException("书架不存在该书，请检查", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        bookShelfInTable.setDeleted(true);
        bookShelfMapper.updateByPrimaryKey(bookShelfInTable);
    }


    @Transactional
    public List<BookShelf> removeBooksFromShelf(List<Integer> bookIds,Integer userId) {

        BookShelfExample bookShelfExample = new BookShelfExample();
        bookShelfExample.createCriteria().andBookIdIn(bookIds).andUserIdEqualTo(userId).andDeletedEqualTo(false);

        List<BookShelf> bookShelves = bookShelfMapper.selectByExample(bookShelfExample);
        if (bookShelves.size() != bookIds.size()) {
            throw new CustomException("存在不在书架中的图书", HttpStatus.INTERNAL_SERVER_ERROR);
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
