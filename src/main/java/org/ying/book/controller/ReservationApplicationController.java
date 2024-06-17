package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
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

    @GetMapping()
    public List<ReservationApplication> getReservationApplicationList(@ModelAttribute ReservationApplicationQueryDto reservationApplicationQueryDto) {
        return reservationApplicationService.getReservationApplicationList(reservationApplicationQueryDto);
    }

    @PostMapping("/{id}")
    public void fulfillReservationApplication(@PathVariable Integer id) {
        reservationApplicationService.fulfillReservationApplication(id);
    }
}
