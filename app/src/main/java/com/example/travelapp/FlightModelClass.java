package com.example.travelapp;

public class FlightModelClass {
    private int id;
    private String airline;
    private String schedule;
    private int seat_count;
    private String origin;
    private String destination;

    // Constructor
    public FlightModelClass(int id, String airline, String schedule, int seat_count, String origin, String destination) {
        this.id = id;
        this.airline = airline;
        this.schedule = schedule;
        this.seat_count = seat_count;
        this.origin = origin;
        this.destination = destination;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getSeat_count() {
        return seat_count;
    }

    public void setSeat_count(int seat_count) {
        this.seat_count = seat_count;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
