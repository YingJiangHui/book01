package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.reservation.ReservationDto;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.Reservation;
import org.ying.book.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/books/reservation")
public class ReservationController {
    @Resource
    private ReservationService reservationService;
    @PostMapping
    public List<Reservation> reserveBooks(ReservationDto reservationDto) {
        if(UserContext.getCurrentUser() == null || UserContext.getCurrentUser().getId() == null){
            throw new CustomException("用户未登录", HttpStatus.UNAUTHORIZED);
        }
        reservationDto.setUserId(UserContext.getCurrentUser().getId());
        return reservationService.reserveBooks(reservationDto);
    }
}
