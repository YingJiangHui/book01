package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.book.BookDto;
import org.ying.book.dto.book.BookQueryDto;
import org.ying.book.dto.book.BookSearchDto;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.mapper.BookFileMapper;
import org.ying.book.mapper.BookMapper;
import org.ying.book.mapper.LibraryBookMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.List;

@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private FileService fileService;

    @Resource
    private BookFileMapper bookFileMapper;

    @Autowired
    private LibraryBookMapper libraryBookMapper;

    public Book getBook(Integer id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        book.setFiles(fileService.filesWithUrl(book.getFiles()));
        return book;
    }

    public List<Book> getBooksByIds(List<Integer> ids) {
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        criteria.andIdIn(ids);
        return bookMapper.selectByExample(bookExample).stream().map((book) -> {
            book.setFiles(fileService.filesWithUrl(book.getFiles()));
            return book;
        }).toList();
    }

    public PageResultDto<Book> getBooksPagination(BookQueryDto bookQueryDto) {
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        if (bookQueryDto.getIds() != null && !bookQueryDto.getIds().isEmpty()) {
            criteria.andIdIn(bookQueryDto.getIds());
        }
        return this.getBooksWithPaginate(bookExample, bookQueryDto);
    }

    public List<Book> getBooksByCategoryId(Integer categoryId) {
        BookExample bookExample = new BookExample();
        bookExample.createCriteria().andCategoryIdEqualTo(categoryId);
        return bookMapper.selectByExample(bookExample).stream().map((book) -> {
            book.setFiles(fileService.filesWithUrl(book.getFiles()));
            return book;
        }).toList();
    }

    public List<Book> getBooksByExampleWithRowbounds(BookExample example, RowBounds rowBounds) {
        return bookMapper.selectByExampleWithRowbounds(example, rowBounds).stream().map((book) -> {
            book.setFiles(fileService.filesWithUrl(book.getFiles()));
            return book;
        }).toList();
    }

    public PageResultDto<Book> getBooksWithPaginate(BookExample example, IPageReq pageReqDto) {
        return PaginationHelper.paginate(pageReqDto, (rowBounds, reqDto) -> this.getBooksByExampleWithRowbounds(example, rowBounds), bookMapper.countByExample(example));
    }

    @Transactional
    public Book createBook(BookDto bookDto) {
//        保存文件
        List<File> files = bookDto.getFile().stream().map(file -> {
            try {
                return fileService.uploadFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
//        创建图书类
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .categoryId(bookDto.getCategoryId())
                .isbn(bookDto.getIsbn())
                .available(bookDto.getAvailable())
                .publisher(bookDto.getPublisher())
                .publishedYear(bookDto.getPublishedYear())
                .description(bookDto.getDescription())
                .files(files)
                .build();

        bookMapper.insertSelective(book);
//        保存图书文件关联
        files.stream().forEach((file) -> {
            bookFileMapper.insertSelective(BookFile.builder().fileId(Long.valueOf(file.getId())).bookId(Long.valueOf(book.getId())).build());
        });

//        保存图书图书馆关联关系
        libraryBookMapper.insertSelective(LibraryBook.builder().libraryId(bookDto.getLibraryId()).bookId(book.getId()).build());

        return book;
    }

    private void searchWith(BookExample.Criteria criteria, BookSearchDto book) {
        if (book.getIds() != null && !book.getIds().isEmpty()) {
            criteria.andIdIn(book.getIds());
        }
        if (book.getPublishedYear() != null) {
            criteria.andPublishedYearLike(book.getPublishedYear());
        }
        if (book.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(book.getCategoryId());
        }
        if (book.getAvailable() != null) {
            criteria.andAvailableEqualTo(book.getAvailable());
        }
        if (book.getDescription() != null) {
            criteria.andDescriptionLike(book.getDescription());
        }
    }

    public PageResultDto<Book> searchBook(BookSearchDto bookSearchDto) {

        if (bookSearchDto.getLibraryId() != null) {
            LibraryBookExample libraryBookExample = new LibraryBookExample();
            libraryBookExample.createCriteria().andLibraryIdEqualTo(bookSearchDto.getLibraryId());
            List<Integer> bookIdList = libraryBookMapper.selectByExample(libraryBookExample).stream().map(LibraryBook::getBookId).toList();
            if (bookIdList != null && !bookIdList.isEmpty()) {
                bookSearchDto.setIds(bookIdList);
            }
        }

        BookExample bookExample = new BookExample();
        BookExample.Criteria criteriaTitle = bookExample.createCriteria();
        criteriaTitle.andTitleLike("%" + bookSearchDto.getKeyword() + "%");
        searchWith(criteriaTitle, bookSearchDto);

        BookExample.Criteria criteriaAuthor = bookExample.createCriteria();
        criteriaAuthor.andAuthorLike("%" + bookSearchDto.getKeyword() + "%");
        searchWith(criteriaAuthor, bookSearchDto);
//
        BookExample.Criteria criteriaPublisher = bookExample.createCriteria();
        criteriaPublisher.andPublisherLike("%" + bookSearchDto.getKeyword() + "%");
        searchWith(criteriaPublisher, bookSearchDto);

        BookExample.Criteria criteriaIsbn = bookExample.createCriteria();
        criteriaIsbn.andIsbnEqualTo("%" + bookSearchDto.getKeyword() + "%");
        searchWith(criteriaIsbn, bookSearchDto);

//        bookExample.setDistinct(true);
        bookExample.or(criteriaTitle);
        bookExample.or(criteriaAuthor);
        bookExample.or(criteriaPublisher);
        bookExample.or(criteriaIsbn);
        return PaginationHelper.paginate(bookSearchDto, (rowBounds, reqDto) -> this.getBooksByExampleWithRowbounds(bookExample, rowBounds), bookMapper.countByExample(bookExample));
    }
}
