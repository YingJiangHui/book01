package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.category.CategoryDto;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.mapper.BookCategoryMapper;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.BookCategory;
import org.ying.book.pojo.BookCategoryExample;
import org.ying.book.utils.PaginationHelper;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    private BookCategoryMapper bookCategoryMapper;

    @Resource
    private BookService bookService;

    public List<BookCategory> getAllCategories() {
        return bookCategoryMapper.selectByExample(new BookCategoryExample());
    }
//    获取图书分类的分页数据
    public PageResultDto<BookCategory> getCategoriesWithPaginate(PageReqDto pageReqDto) {
        BookCategoryExample example = new BookCategoryExample();
        return PaginationHelper.paginate(pageReqDto, (rowBounds, pageDto) -> bookCategoryMapper.selectByExampleWithRowbounds(example, rowBounds), bookCategoryMapper.countByExample(example));
    }
//    创建图书分类
    @Transactional
    public BookCategory createCategory(CategoryDto categoryDto) {
        BookCategory category = BookCategory.builder().categoryName(categoryDto.getName()).description(categoryDto.getDescription()).build();
        bookCategoryMapper.insertSelective(category);
        return category;
    }

    public List<Book> getBooksByCategoryId(Integer categoryId){
        return bookService.getBooksByCategoryId(categoryId);
    }

}
