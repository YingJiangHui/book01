package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.library.BooksInLibraryDto;
import org.ying.book.dto.library.LibraryDto;
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
    public Library insertLibrary(Library library) {
        if(library.getClosed()!=null){
            if(library.getClosed()){
                library.setDisableBorrow(true);
                library.setDisableReserve(true);
            }
        }
        libraryMapper.insertSelective(library);
        return library;
    }

    @Transactional
    public Library updateLibrary(Library library) {
        if(library.getClosed()!=null){
            if(library.getClosed()){
                library.setDisableBorrow(true);
                library.setDisableReserve(true);
            }
        }
        libraryMapper.updateByPrimaryKeySelective(library);
        return library;
    }


    public Library getLibraryById(int id) {
        return libraryMapper.selectByPrimaryKey(id);
    }

    public PageResultDto<Library> getLibraries(LibraryDto libraryDto) {
        LibraryExample example = new LibraryExample();
        example.setOrderByClause("created_at desc");
        LibraryExample.Criteria criteria = example.createCriteria();
        if (libraryDto.getId() != null) {
            criteria.andIdEqualTo(libraryDto.getId());
        }
        if (libraryDto.getAddress() != null && !libraryDto.getAddress().isEmpty()) {
            criteria.andAddressLike("%"+libraryDto.getAddress()+"%");
        }
        if (libraryDto.getName() != null && !libraryDto.getName().isEmpty()) {
            criteria.andNameLike("%"+libraryDto.getName().trim()+"%");
        }
        if (libraryDto.getClosed() != null ) {
            criteria.andClosedEqualTo(libraryDto.getClosed());
        }
        criteria.andDeletedNotEqualTo(true);

        return PaginationHelper.paginate(new PageReqDto(), (rowBounds1, pageDto) -> libraryMapper.selectByExampleWithRowbounds(example, rowBounds1), libraryMapper.countByExample(example));
    }

    public List<Library> getLibrariesAll(){
        LibraryExample example = new LibraryExample();
        example.createCriteria().andDeletedEqualTo(false);
        return libraryMapper.selectByExample(example);
    }

    @Transactional
    public void userRelativeLibraries(Integer userId, List<Integer> libraryIds) {
        Optional.ofNullable(libraryIds).ifPresent(ids->{
            if(ids.stream().map(libraryId-> libraryMapper.selectByPrimaryKey(libraryId)).anyMatch(Objects::isNull)){
                throw new RuntimeException("图书馆不存在");
            }
            ids.forEach(roleName -> {
                LibraryUserExample libraryUserExample = new LibraryUserExample();
                libraryUserExample.createCriteria().andUserIdEqualTo(userId);
                libraryUserMapper.deleteByExample(libraryUserExample);
            });
            ids.stream()
                    .map(libraryId -> LibraryUser.builder().userId(userId).libraryId(libraryId).build())
                    .forEach(userRole -> libraryUserMapper.insertSelective(userRole));
        });

    }

    public PageResultDto<Book> getAllBooksInLibrary(Integer libraryId, BooksInLibraryDto booksInLibraryDto){
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
        bookExample.setOrderByClause("created_at desc");
        bookService.withCriteria(bookExample,criteria,booksInLibraryDto);
        return bookService.getBooksWithPaginate(bookExample,booksInLibraryDto);
    }
}
