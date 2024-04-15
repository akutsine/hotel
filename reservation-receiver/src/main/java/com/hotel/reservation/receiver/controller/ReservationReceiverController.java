package com.hotel.reservation.receiver.controller;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.receiver.service.ReservationQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationReceiverController {
    Logger logger = LoggerFactory.getLogger(ReservationReceiverController.class);

    private ReservationQueueService queueService;

    @Autowired
    public ReservationReceiverController(ReservationQueueService service) {
        this.queueService = service;
    }

    @PostMapping("/send-reservation")
    public ResponseEntity receiveReservationForProcessing(
            @RequestBody String reservationMessage
    ) {
        Reservation reservation = new Reservation(
                reservationMessage,
                System.currentTimeMillis()
        );

        try {
            queueService.sendToQueue(reservation);
        } catch (Exception e) {
            logger.error("Reservation " + reservation + " was sent to process with an error", e);
            return ResponseEntity.internalServerError().build();
        }

        logger.info("Reservation " + reservation + " was sent to Processing queue");
        return ResponseEntity.ok("OK");
    }
}
