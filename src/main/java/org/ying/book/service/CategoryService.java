package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.category.CategoryDto;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.exception.CustomException;
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
        BookCategoryExample bookCategoryExample = new BookCategoryExample();
        bookCategoryExample.createCriteria().andDeletedEqualTo(false);
        return bookCategoryMapper.selectByExample(bookCategoryExample);
    }
//    获取图书分类的分页数据
    public PageResultDto<BookCategory> getCategoriesWithPaginate(PageReqDto pageReqDto) {
        BookCategoryExample example = new BookCategoryExample();
        example.createCriteria().andDeletedEqualTo(false);
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

    public void deleteCategory(Integer id) {
        List<Book> bookList = bookService.getBooksByCategoryId(id);
        if(bookList!=null && !bookList.isEmpty()){
            throw new CustomException("分类已关联图书，无法删除");
        }

        BookCategory bookCategory = bookCategoryMapper.selectByPrimaryKey(id);
        bookCategory.setDeleted(true);
        bookCategoryMapper.updateByPrimaryKeySelective(bookCategory);
    }

}
