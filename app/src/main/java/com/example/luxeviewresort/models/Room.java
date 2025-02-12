package com.example.luxeviewresort.models;

public class Room {
    private int id;
    private String name;
    private double price;
    private String image; // Added Image Field

    // Constructor with Image
    public Room(int id, String name, double price, String image) {
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
        this.image = ""; // Default empty image
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

    public String getImage() {
        return image;
    }

    // Setters
    public void setImage(String image) {
        this.image = image;
    }
}