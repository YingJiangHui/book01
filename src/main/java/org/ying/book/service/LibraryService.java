package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.email.EmailBorrowNotificationDto;
import org.ying.book.dto.library.BooksInLibraryDto;
import org.ying.book.dto.library.LibraryDto;
import org.ying.book.enums.ReservationApplicationEnum;
import org.ying.book.enums.ReservationStatusEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.*;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.*;

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
    @Autowired
    private BorrowingService borrowingService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationApplicationService reservationApplicationService;
    @Autowired
    private ReservationApplicationMapper reservationApplicationMapper;
    @Autowired
    private ReservationViewMapper reservationViewMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Transactional
    public Library insertLibrary(Library library) {
        if (library.getClosed() != null) {
            if (library.getClosed()) {

                library.setDisableBorrow(true);
                library.setDisableReserve(true);
                library.setDisableReserveApplication(true);
            }
        }
        libraryMapper.insertSelective(library);
        return library;
    }

    public void closeBeforeLibraryCheck(Library library) {
        LibraryBookExample libraryBookExample = new LibraryBookExample();
        libraryBookExample.createCriteria().andLibraryIdEqualTo(library.getId());
        libraryBookMapper.selectByExample(libraryBookExample).forEach(libraryBook -> {
            List<BorrowingView> borrowingViewList = borrowingService.getCurrentBorrowedBook(libraryBook.getBookId());
            if (borrowingViewList != null && !borrowingViewList.isEmpty()) {
                throw new CustomException("该图书馆存在为归还的图书，无法关闭");
            }
        });
    }

    @Transactional
    public void closeAfterLibraryHandler(Library library) {
//        取消所有的预订
        ReservationViewExample reservationViewExample = new ReservationViewExample();
        reservationViewExample.createCriteria().andLibraryIdEqualTo(library.getId()).andStatusIn(Arrays.asList(ReservationStatusEnum.BORROWABLE.name(), ReservationStatusEnum.NOT_BORROWABLE.name()));
        List<ReservationView> reservationViewList = reservationViewMapper.selectByExample(reservationViewExample);
        List<Integer> reservationIds = reservationViewList.stream().map(ReservationView::getId).toList();
        if(!reservationIds.isEmpty()){
            reservationService.cancelReservations(reservationIds);
        }

//        取消所有的预约
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.createCriteria().andLibraryIdEqualTo(library.getId()).andStatusIn(Arrays.asList(ReservationApplicationEnum.PENDING.name(), ReservationApplicationEnum.NOTIFIED.name()));
        List<ReservationApplication> reservationApplicationList = reservationApplicationMapper.selectByExample(reservationApplicationExample);

        reservationApplicationList.forEach(reservationApplication -> {
            reservationApplicationService.cancelReservationApplication(reservationApplication.getId());
        });

//         统一发送取消短信
        reservationViewList.forEach((reservationView) -> {
            User user = userService.getUser(reservationView.getUserId());
            Book book = bookService.getBook(reservationView.getBookId());
            Optional.ofNullable(user).ifPresent((u) -> {
                EmailBorrowNotificationDto.builder().libraryName(library.getName()).email(user.getEmail());
                try {
                    emailService.sendCloseLibraryNotificationEmail(EmailBorrowNotificationDto.builder().bookName(book.getTitle()).libraryName(library.getName()).email(user.getEmail()).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        reservationApplicationList.forEach((reservationApplication) -> {
            User user = userService.getUser(reservationApplication.getUserId());
            Book book = bookService.getBook(reservationApplication.getBookId());
            Optional.ofNullable(user).ifPresent((u) -> {
                try {
                    emailService.sendCloseLibraryNotificationEmail(EmailBorrowNotificationDto.builder().bookName(book.getTitle()).libraryName(library.getName()).email(user.getEmail()).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

    }

    @Transactional
    public Library updateLibrary(Library library) {
        Library oldLibrary = libraryMapper.selectByPrimaryKey(library.getId());
        if (library.getClosed() != null) {
            if (library.getClosed() && !oldLibrary.getClosed()) {
                closeBeforeLibraryCheck(library);
                library.setDisableBorrow(true);
                library.setDisableReserve(true);
                library.setDisableReserveApplication(true);
            }
        }
        libraryMapper.updateByPrimaryKeySelective(library);
        if (library.getClosed() != null) {
            if (library.getClosed() && !oldLibrary.getClosed()) {
                closeAfterLibraryHandler(library);
            }
        }
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
            criteria.andAddressLike("%" + libraryDto.getAddress() + "%");
        }
        if (libraryDto.getName() != null && !libraryDto.getName().isEmpty()) {
            criteria.andNameLike("%" + libraryDto.getName().trim() + "%");
        }
        if (libraryDto.getClosed() != null) {
            criteria.andClosedEqualTo(libraryDto.getClosed());
        }
        criteria.andDeletedNotEqualTo(true);

        return PaginationHelper.paginate(new PageReqDto(), (rowBounds1, pageDto) -> libraryMapper.selectByExampleWithRowbounds(example, rowBounds1), libraryMapper.countByExample(example));
    }

    public List<Library> getLibrariesAll() {
        LibraryExample example = new LibraryExample();
        example.createCriteria().andDeletedEqualTo(false);
        return libraryMapper.selectByExample(example);
    }

    @Transactional
    public void userRelativeLibraries(Integer userId, List<Integer> libraryIds) {
        Optional.ofNullable(libraryIds).ifPresent(ids -> {
            if (ids.stream().map(libraryId -> libraryMapper.selectByPrimaryKey(libraryId)).anyMatch(Objects::isNull)) {
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

    public PageResultDto<Book> getAllBooksInLibrary(Integer libraryId, BooksInLibraryDto booksInLibraryDto) {
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
        bookService.withCriteria(bookExample, criteria, booksInLibraryDto);
        return bookService.getBooksWithPaginate(bookExample, booksInLibraryDto);
    }

    public Library getLibraryByBookId(Integer bookId) {
        LibraryBookExample libraryBookExample = new LibraryBookExample();
        libraryBookExample.createCriteria().andBookIdEqualTo(bookId);
        List<LibraryBook> libraryBooks = libraryBookMapper.selectByExample(libraryBookExample);
        if (libraryBooks.isEmpty()) {
            return null;
        }
        return libraryMapper.selectByPrimaryKey(libraryBooks.get(0).getLibraryId());
    }
}
