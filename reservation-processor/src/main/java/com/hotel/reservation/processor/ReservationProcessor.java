package com.hotel.reservation.processor;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.processor.buffer.ReservationBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationProcessor {
    Logger logger = LoggerFactory.getLogger(ReservationProcessor.class);

    private ReservationBuffer reservationBuffer;

    @Autowired
    public ReservationProcessor(ReservationBuffer reservationBuffer) {
        this.reservationBuffer = reservationBuffer;
    }


    @JmsListener(destination = "${jms.queue.name}")
    public void processQueue(Reservation reservation) {
        reservationBuffer.addMessage(reservation);
    }
}
