package com.hotel.reservation.processor.buffer;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.processor.service.ReservationProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReservationBufferTest {

    private ReservationBuffer reservationBuffer;

    @Mock
    private ReservationProcessService reservationProcessServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationBuffer = new ReservationBuffer(100, 1000, reservationProcessServiceMock);
    }

    @Test
    public void testBufferCapacityLimit() {
        List<Reservation> reservations = IntStream.rangeClosed(1, 101)
                .mapToObj(i -> new Reservation("message" + i, System.currentTimeMillis()+i))
                .toList();
        reservations
                .forEach(reservationBuffer::addMessage);

        reservations.stream().forEach(verify(reservationProcessServiceMock, times(1))::process);
    }

    @Test
    public void testBufferFlushInterval() throws InterruptedException {
        List<Reservation> reservations = IntStream.rangeClosed(1, 4)
                .mapToObj(i -> new Reservation("message" + i, System.currentTimeMillis()+i))
                .toList();
        reservations
                .forEach(reservationBuffer::addMessage);
        Thread.sleep(2000);
        reservations.stream().forEach(verify(reservationProcessServiceMock, times(1))::process);
    }

    @Test
    public void testAddingReservations() throws InterruptedException{
        int quantity = 5;
        long [] shifts = {14, -5, 0, 1, -20, -40};
        List<Reservation> reservations = IntStream.rangeClosed(0, quantity)
                .mapToObj(i -> new Reservation("message" + i, 1 + shifts[i]))
                .toList();
        reservations
                .forEach(reservationBuffer::addMessage);

        Thread.sleep(2000);

        Map<Long, Reservation> reservationByStamp =
                reservations.stream().collect(Collectors.toMap(Reservation::timestamp, item -> item));

        long [] sortedStamps = Arrays.stream(shifts).sorted().map(i -> i + 1).toArray();
        Arrays.stream(sortedStamps).forEach(System.out::print);

        InOrder orderVerifier = Mockito.inOrder(reservationProcessServiceMock);
        Arrays.stream(sortedStamps).mapToObj(reservationByStamp::get).forEach(reservation -> {
            orderVerifier.verify(reservationProcessServiceMock).process(reservation);
        });
    }
}

