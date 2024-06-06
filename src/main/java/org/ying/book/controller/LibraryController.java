package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.library.BooksInLibraryDto;
import org.ying.book.dto.library.LibraryDto;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.Library;
import org.ying.book.service.LibraryService;

import java.util.List;

@RestController
@RequestMapping(value = "/library")
public class LibraryController {
    @Resource
    private LibraryService libraryService;

    @PostMapping()
    public Library createLibrary(@RequestBody Library library) {
        return libraryService.insertLibrary(library);
    }

    @PatchMapping()
    public Library updateLibrary(@RequestBody Library library) {
        return libraryService.updateLibrary(library);
    }

    @GetMapping("{libraryId}")
    public Library getLibrary(@PathVariable("libraryId") int libraryId) {
        return libraryService.getLibraryById(libraryId);
    }

    @GetMapping()
    public PageResultDto<Library> getLibraries(LibraryDto libraryDto) {
        return libraryService.getLibraries(libraryDto);
    }

    @GetMapping("/all")
    public List<Library> getLibrariesAll() {
        return libraryService.getLibrariesAll();
    }

    @GetMapping("{libraryId}/books")
    public PageResultDto<Book> getBooksInLibrary(@PathVariable("libraryId") int libraryId,@ModelAttribute BooksInLibraryDto booksInLibraryDto){
        return libraryService.getAllBooksInLibrary(libraryId,booksInLibraryDto);
    }

}
