package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.ying.book.dto.user.UserDto;
import org.ying.book.pojo.Library;
import org.ying.book.service.LibraryService;
import org.ying.book.utils.Result;

import java.util.List;

@RestController
@RequestMapping(value = "/library")
public class LibraryController {
    @Resource
    private LibraryService libraryService;

    @PostMapping()
    public void createLibrary(@RequestBody Library library) {
        libraryService.insertLibrary(library);
    }

    @GetMapping("{libraryId}")
    public Library getLibrary(@PathVariable("libraryId") int libraryId) {
        return libraryService.getLibraryById(libraryId);
    }

    @GetMapping()
    public List<Library> getLibraries() {
        return libraryService.getLibraries();
    }

}
