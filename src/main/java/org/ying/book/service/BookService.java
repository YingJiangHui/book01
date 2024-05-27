package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.book.BookDto;
import org.ying.book.dto.book.BookQueryDto;
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

    public PageResultDto<Book> getBooksPagination(BookQueryDto bookQueryDto) {
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        if(bookQueryDto.getIds() != null && !bookQueryDto.getIds().isEmpty()){
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

    public PageResultDto<Book> getBooksWithPaginate(BookExample example, PageReqDto pageReqDto) {
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
}
