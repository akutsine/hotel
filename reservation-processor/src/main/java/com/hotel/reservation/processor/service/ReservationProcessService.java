package com.hotel.reservation.processor.service;

import com.hotel.reservation.model.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservationProcessService {

    Logger logger = LoggerFactory.getLogger(ReservationProcessService.class);

    public void process(Reservation reservation) {
        logger.info("Processing of reservation: " + reservation);
    }
}
