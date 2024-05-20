package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.mapper.LibraryBookMapper;
import org.ying.book.mapper.LibraryMapper;
import org.ying.book.mapper.LibraryUserMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class LibraryService {
    @Resource
    private LibraryMapper libraryMapper;

    @Resource
    private LibraryUserMapper libraryUserMapper;

    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @Resource
    private BookService bookService;

    @Transactional
    public void insertLibrary(Library library) {
        libraryMapper.insertSelective(library);
    }

    public Library getLibraryById(int id) {
        return libraryMapper.selectByPrimaryKey(id);
    }

    public List<Library> getLibraries() {
        LibraryExample example = new LibraryExample();
        RowBounds rowBounds = new RowBounds(0, 100);
        return libraryMapper.selectByExampleWithRowbounds(example,rowBounds);
    }

    @Transactional
    public void userRelativeLibraries(Integer userId, List<Integer> libraryIds) {
        Optional.ofNullable(libraryIds).ifPresent(ids->{
            if(ids.stream().map(libraryId-> libraryMapper.selectByPrimaryKey(libraryId)).anyMatch(Objects::isNull)){
                throw new RuntimeException("图书馆不存在");
            }
            ids.stream()
                    .map(libraryId -> LibraryUser.builder().userId(userId).libraryId(libraryId).build())
                    .forEach(userRole -> libraryUserMapper.insertSelective(userRole));
        });

    }

    public PageResultDto<Book> getAllBooksInLibrary(Integer libraryId, PageReqDto pageReqDto){
        LibraryBookExample libraryBookExample = new LibraryBookExample();
        libraryBookExample.createCriteria().andLibraryIdEqualTo(libraryId);
        List<Integer> bookIdList = libraryBookMapper.selectByExample(libraryBookExample).stream().map(LibraryBook::getBookId).toList();
        if (bookIdList.isEmpty()) {
            // Return an empty result or handle it in another way
            return new PageResultDto<Book>();
        }
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        criteria.andIdIn(bookIdList);

        return bookService.getBooksWithPaginate(bookExample, pageReqDto);
    }
}
