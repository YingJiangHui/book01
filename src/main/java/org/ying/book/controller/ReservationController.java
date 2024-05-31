package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.reservation.ReservationDto;
import org.ying.book.dto.reservation.ReservationQueryDto;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.Reservation;
import org.ying.book.pojo.ReservationView;
import org.ying.book.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/books/reservation")
public class ReservationController {
    @Resource
    private ReservationService reservationService;

    @PostMapping
    public List<Reservation> reserveBooks(@RequestBody ReservationDto reservationDto) {
        if(UserContext.getCurrentUser() == null || UserContext.getCurrentUser().getId() == null){
            throw new CustomException("用户未登录", HttpStatus.UNAUTHORIZED);
        }
        reservationDto.setUserId(UserContext.getCurrentUser().getId());
        return reservationService.reserveBooks(reservationDto);
    }

    @PostMapping("/cancel")
    public List<Reservation> cancelReservation(List<Integer> ids) {
        return reservationService.cancelReservations(ids);
    }

    @GetMapping
    public List<ReservationView> getReservations(@ModelAttribute ReservationQueryDto reservationQueryDto) {
        UserJwtDto userJwtDto = UserContext.getCurrentUser();
//        如果是用户进行查询
        if(userJwtDto.getRoles().size() == 1 && userJwtDto.getRoles().contains(RoleEnum.READER)){
            reservationQueryDto.setUserId(userJwtDto.getId());
        }
        return reservationService.getReservations(reservationQueryDto);
    }
}
