package com.example.haircutapp;

public class Appointment {
    private String date;
    private String time;
    private String customerName;
    // Add other fields as needed

    // Default constructor (required for Firebase)
    public Appointment() {}

    // Constructor
    public Appointment(String date, String time, String customerName) {
        this.date = date;
        this.time = time;
        this.customerName = customerName;
    }

}
