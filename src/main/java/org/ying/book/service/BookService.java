package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.book.BookDto;
import org.ying.book.dto.book.BookQueryDto;
import org.ying.book.dto.book.BookSearchDto;
import org.ying.book.dto.category.CategoryQueryDto;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.enums.ReservationStatusEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.*;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Resource
    private ReservationService reservationService;

    @Resource
    private BorrowingService borrowingService;
    @Autowired
    private ReservationViewMapper reservationViewMapper;

    @Resource
    private ReservationApplicationService reservationApplicationService;

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

    public void withCriteria(BookExample bookExample,BookExample.Criteria criteria,BookQueryDto bookQueryDto){
        if(bookQueryDto.getId() != null){
            criteria.andIdEqualTo(bookQueryDto.getId());
        }
        if (bookQueryDto.getFirstLibraryId() != null) {
            bookExample.setOrderByClause(String.format("CASE \n" +
                    "        WHEN l.id = %d THEN 0\n" +
                    "        ELSE 1\n" +
                    "    END", bookQueryDto.getFirstLibraryId()));
        }

        if(bookQueryDto.getTitle()!=null && !bookQueryDto.getTitle().isEmpty()){
            criteria.andTitleLike("%"+bookQueryDto.getTitle()+"%");
        }

        if (bookQueryDto.getIsBanner()!=null){
            criteria.andIsBannerEqualTo(bookQueryDto.getIsBanner());
        }

        if(bookQueryDto.getIsRecommend()!=null){
            criteria.andIsRecommendEqualTo(bookQueryDto.getIsRecommend());
        }

        if (bookQueryDto.getIds() != null && !bookQueryDto.getIds().isEmpty()) {
            criteria.andIdIn(bookQueryDto.getIds());
        }
        if (bookQueryDto.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(bookQueryDto.getCategoryId());
        }
        if (bookQueryDto.getAvailable() != null) {
            criteria.andAvailableEqualTo(bookQueryDto.getAvailable());
        }
        if (bookQueryDto.getAuthor() != null&& !bookQueryDto.getAuthor().isEmpty()) {
            criteria.andAuthorLike("%"+bookQueryDto.getAuthor()+"%");
        }
        if (bookQueryDto.getPublisher() != null&& !bookQueryDto.getPublisher().isEmpty()) {
            criteria.andPublisherLike("%"+bookQueryDto.getPublisher()+"%");
        }
        if (bookQueryDto.getIsbn() != null&& !bookQueryDto.getIsbn().isEmpty()) {
            criteria.andIsbnEqualTo(bookQueryDto.getIsbn());
        }
        if(bookQueryDto.getPublishedYear()!=null && !bookQueryDto.getPublishedYear().isEmpty()){
            criteria.andPublishedYearLike("%"+bookQueryDto.getPublishedYear()+"%");
        }
    }

    public PageResultDto<Book> getBooksPagination(BookQueryDto bookQueryDto) {
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        withCriteria(bookExample,criteria,bookQueryDto);
        return this.getBooksWithPaginate(bookExample, bookQueryDto);
    }

    public List<Book> getBooksByCategoryId(Integer categoryId) {
        return this.getBooksByCategoryId(categoryId,null);
    }

    public PageResultDto<Book> getBooksByCategoryIdPagination(Integer categoryId, CategoryQueryDto categoryQueryDto) {
        BookExample bookExample = new BookExample();
        if (categoryQueryDto.getFirstLibraryId() != null) {
            bookExample.setOrderByClause(String.format("CASE \n" +
                    "        WHEN l.id = %d THEN 0\n" +
                    "        ELSE 1\n" +
                    "    END", categoryQueryDto.getFirstLibraryId()));
        }
        bookExample.createCriteria().andCategoryIdEqualTo(categoryId);
        return PaginationHelper.paginate(categoryQueryDto, (rowBounds, reqDto) -> this.getBooksByExampleWithRowbounds(bookExample, rowBounds), bookMapper.countByExample(bookExample));
    }

    public List<Book> getBooksByCategoryId(Integer categoryId,Integer firstLibraryId) {
        BookExample bookExample = new BookExample();
        if (firstLibraryId != null) {
            bookExample.setOrderByClause(String.format("CASE \n" +
                    "        WHEN l.id = %d THEN 0\n" +
                    "        ELSE 1\n" +
                    "    END", firstLibraryId));
        }
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
                .build();

        BeanUtils.copyProperties(bookDto, book);


        bookMapper.insertSelective(book);
//        保存图书文件关联
        files.stream().forEach((file) -> {
            bookFileMapper.insertSelective(BookFile.builder().fileId(Long.valueOf(file.getId())).bookId(Long.valueOf(book.getId())).build());
        });

//        保存图书图书馆关联关系
        libraryBookMapper.insertSelective(LibraryBook.builder().libraryId(bookDto.getLibraryId()).bookId(book.getId()).build());

        return book;
    }

    @Transactional
    public void updateBook(Integer id, BookDto bookDto) {
        Book book = bookMapper.selectByPrimaryKey(id);
        if (book == null) {
            throw new CustomException("图书不存在");
        }

        if (!Objects.equals(bookDto.getLibraryId(), book.getLibrary().getId())) {
            LibraryBookExample libraryBookExample = new LibraryBookExample();
            libraryBookExample.createCriteria().andBookIdEqualTo(id).andLibraryIdEqualTo(book.getLibrary().getId());
            libraryBookMapper.deleteByExample(libraryBookExample);
            libraryBookMapper.insertSelective(LibraryBook.builder().libraryId(bookDto.getLibraryId()).bookId(book.getId()).build());
        }

//        删除所有未匹配的旧file文件
        BookFileExample bookFileExample = new BookFileExample();
        BookFileExample.Criteria criteria = bookFileExample.createCriteria();
        criteria.andBookIdEqualTo(Long.valueOf(id));
        Optional.ofNullable(bookDto.getOldFileIds()).ifPresent(ids -> {
            criteria.andFileIdNotIn(ids.stream().map(Long::valueOf).toList());
        });
        bookFileMapper.deleteByExample(bookFileExample);


        if (bookDto.getFile() != null) {
            List<File> files = bookDto.getFile().stream().map(file -> {
                try {
                    return fileService.uploadFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).toList();
            files.stream().forEach((file) -> {
                bookFileMapper.insertSelective(BookFile.builder().fileId(Long.valueOf(file.getId())).bookId(Long.valueOf(book.getId())).build());
            });
        }

        BeanUtils.copyProperties(bookDto, book);
        bookMapper.updateByPrimaryKeySelective(book);
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
        if(bookSearchDto.getKeyword()!=null&&!bookSearchDto.getKeyword().isEmpty()){
            criteriaIsbn.andIsbnEqualTo(bookSearchDto.getKeyword());
            searchWith(criteriaIsbn, bookSearchDto);
        }
//        bookExample.setDistinct(true);
        bookExample.or(criteriaTitle);
        bookExample.or(criteriaAuthor);
        bookExample.or(criteriaPublisher);
        bookExample.or(criteriaIsbn);
        return PaginationHelper.paginate(bookSearchDto, (rowBounds, reqDto) -> this.getBooksByExampleWithRowbounds(bookExample, rowBounds), bookMapper.countByExample(bookExample));
    }

    // 下架图书
    @Transactional
    public void deleteBook(Integer id) {
        List<ReservationApplication> reservationApplicationList = reservationApplicationService.getCurrentReservedBook(id);
        if (reservationApplicationList != null && !reservationApplicationList.isEmpty()) {
            throw new CustomException("书籍已被预约，无法下架");
        }

        List<BorrowingView> borrowingViewList = borrowingService.getCurrentBorrowedBook(id);
        if (borrowingViewList != null && !borrowingViewList.isEmpty()) {
            throw new CustomException("书籍已被借阅，无法下架");
        }

        List<ReservationView> reservationViewList = reservationService.getCurrentReservedBook(id);
        if (reservationViewList != null && !reservationViewList.isEmpty()) {
            throw new CustomException("书籍已被预订，无法下架");
        }



        Book book = bookMapper.selectByPrimaryKey(id);
        book.setAvailable(false);
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Transactional
    public void recoverBook(Integer id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        if (book.getAvailable()) {
            throw new CustomException("书籍已上架");
        }
        book.setAvailable(true);
        bookMapper.updateByPrimaryKeySelective(book);
    }
}
