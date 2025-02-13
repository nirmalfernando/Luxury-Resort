package com.example.luxeviewresort.models;

import android.graphics.Bitmap;

public class Room {
    private int id;
    private String name;
    private double price;
    private Bitmap image; // Change from String to Bitmap

    // Constructor with Image
    public Room(int id, String name, double price, Bitmap image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    // Existing Constructor for Backward Compatibility
    public Room(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = null; // Default null image
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Bitmap getImage() { // Change return type from String to Bitmap
        return image;
    }

    // Setter
    public void setImage(Bitmap image) {
        this.image = image;
    }
}
