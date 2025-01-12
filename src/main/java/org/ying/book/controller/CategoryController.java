package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.ying.book.dto.category.CategoryDto;
import org.ying.book.dto.category.CategoryQueryDto;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.BookCategory;
import org.ying.book.service.CategoryService;

import java.util.List;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("all")
    public List<BookCategory> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping()
    public PageResultDto<BookCategory> getCategories(@ModelAttribute PageReqDto pageReqDto) {
        return categoryService.getCategoriesWithPaginate(pageReqDto);
    }

    @PostMapping()
    public BookCategory createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @GetMapping("/{id}/books")
    public List<Book> getBooksByCategoryId(@PathVariable("id") Integer id, @RequestParam(value = "firstLibraryId", required = false) Integer firstLibraryId) {
        return categoryService.getBooksByCategoryId(id,firstLibraryId);
    }

    @GetMapping("/{id}/books/pagination")
    public PageResultDto<Book> getBooksByCategoryIdPagination(@PathVariable("id") Integer id, @ModelAttribute CategoryQueryDto categoryQueryDto) {
        return categoryService.getBooksByCategoryIdPagination(id,categoryQueryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }
}
