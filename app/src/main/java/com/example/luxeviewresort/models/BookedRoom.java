package com.example.luxeviewresort.models;

import android.graphics.Bitmap;

public class BookedRoom extends Room {
    private String bookingDate;
    private String bookingTime;

    public BookedRoom(int id, String name, double price, Bitmap image, String bookingDate, String bookingTime) {
        super(id, name, price, image);
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
    }

    public BookedRoom(int id, String name, double price, String bookingDate, String bookingTime) {
        super(id, name, price);
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }
}