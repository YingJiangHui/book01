package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.mapper.LibraryMapper;
import org.ying.book.mapper.LibraryUserMapper;
import org.ying.book.pojo.LibraryUser;

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
}
