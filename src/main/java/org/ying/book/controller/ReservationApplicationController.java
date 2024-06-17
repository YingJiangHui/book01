package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
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
        reservationApplicationService.reservationApply(reservationApplication);
    }

    @GetMapping()
    public List<ReservationApplication> getReservationApplicationList(@RequestParam Integer bookId, @RequestParam List<ReservationApplicationEnum> statusList) {
        return reservationApplicationService.getReservationApplicationList(bookId, statusList);
    }
}
