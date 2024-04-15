package com.hotel.reservation.model;

public record Reservation(String message, long timestamp) implements Comparable<Reservation>{
    @Override
    public int compareTo(Reservation o) {
        return Long.compare(timestamp, o.timestamp);
    }
}
