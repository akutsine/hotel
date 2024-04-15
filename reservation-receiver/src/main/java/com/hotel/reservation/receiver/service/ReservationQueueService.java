package com.hotel.reservation.receiver.service;

import com.hotel.reservation.model.Reservation;
import jakarta.jms.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationQueueService {
    private JmsTemplate jmsTemplate;
    private Destination queueDestination;

    @Autowired
    public ReservationQueueService(JmsTemplate template, Destination queueDestination) {
        this.jmsTemplate = template;
        this.queueDestination = queueDestination;
    }

    public void sendToQueue(Reservation reservation) throws JmsException {
        jmsTemplate.convertAndSend(queueDestination ,reservation);
    }
}
