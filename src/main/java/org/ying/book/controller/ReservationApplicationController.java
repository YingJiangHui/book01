package org.ying.book.controller;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.reservationApplication.ReservationApplicationQueryDto;
import org.ying.book.enums.ReservationApplicationEnum;
import org.ying.book.pojo.ReservationApplication;
import org.ying.book.service.ReservationApplicationService;

import java.util.List;

@RestController
@RequestMapping("/reservation-application")
public class ReservationApplicationController {

    @Resource
    private ReservationApplicationService reservationApplicationService;

    @PostMapping()
    public void applyReservation(@RequestBody ReservationApplication reservationApplication) {
         if(UserContext.getCurrentUser().getId()!=null){
             reservationApplication.setUserId(UserContext.getCurrentUser().getId());
         }
        reservationApplicationService.reservationApply(reservationApplication);
    }

    @GetMapping("/all")
    public List<ReservationApplication> getReservationApplicationListAll(@ModelAttribute ReservationApplicationQueryDto reservationApplicationQueryDto) {
        if(UserContext.getCurrentUser().getId()!=null){
            reservationApplicationQueryDto.setUserId(UserContext.getCurrentUser().getId());
        }
        return reservationApplicationService.getReservationApplicationList(reservationApplicationQueryDto);
    }

    @GetMapping()
    public PageResultDto<ReservationApplication> getReservationApplicationList(@ModelAttribute ReservationApplicationQueryDto reservationApplicationQueryDto) {
        return reservationApplicationService.getReservationApplicationListPagination(reservationApplicationQueryDto);
    }

    @PostMapping("/{id}")
    public void fulfillReservationApplication(@PathVariable Integer id) {
        reservationApplicationService.fulfillReservationApplication(id);
    }

    @DeleteMapping("/{id}")
    public void cancelReservationApplication(@PathVariable Integer id) {
        reservationApplicationService.cancelReservationApplication(id);
    }

    @PostMapping("/{id}/resend-notification")
    public void resendNotification(@PathVariable Integer id) throws MessagingException {
        reservationApplicationService.resendNotification(id);
    }
}
