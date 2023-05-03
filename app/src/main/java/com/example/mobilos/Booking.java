package com.example.mobilos;

public class Booking {
    private int year;
    private int month;
    private int day;
    private int startHour;
    private int endHour;

    // Default constructor (required for Firestore)
    public Booking() {
    }

    // Parameterized constructor
    public Booking(int year, int month, int day, int startHour, int endHour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    // Getters and setters (required for Firestore)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
