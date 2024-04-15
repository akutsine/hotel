package com.hotel.reservation.processor.buffer;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.processor.service.ReservationProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class ReservationBuffer {
    private SortedSet<Reservation> buffer;
    private int capacity;
    private long flushInterval;
    private Timer flushTimer;
    private ReservationProcessService reservationProcessService;

    @Autowired
    public ReservationBuffer(
            @Value("${buffer.capacity}") int capacity,
            @Value("${buffer.flushInterval}") long flushInterval,
            ReservationProcessService reservationProcessService
    ) {
        this.capacity = capacity;
        this.flushInterval = flushInterval;
        this.buffer = Collections.synchronizedSortedSet(new TreeSet());
        this.flushTimer = new Timer();
        this.reservationProcessService = reservationProcessService;
        startFlushTimer();
    }

    public void addMessage(Reservation reservation) {
        buffer.add(reservation);
        if (buffer.size() >= capacity) {
            processAndFlushBuffer();
        }
    }

    private void startFlushTimer() {
        flushTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                processAndFlushBuffer();
            }
        }, flushInterval, flushInterval);
    }

    private void processAndFlushBuffer() {
        buffer.forEach(reservationProcessService::process);
        System.out.println("Processing messages: " + buffer.stream().map(Reservation::timestamp).toList());
        buffer.clear();
    }
}

