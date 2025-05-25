// Car.java
package com.carrental;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private double dailyRate;
    private boolean isAvailable;

    public Car(String make, String model, int year, String licensePlate, double dailyRate) {
        this.id = idGenerator.incrementAndGet();
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.dailyRate = dailyRate;
        this.isAvailable = true; // New cars are available by default
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Car ID: " + id +
               ", Make: '" + make + '\'' +
               ", Model: '" + model + '\'' +
               ", Year: " + year +
               ", License Plate: '" + licensePlate + '\'' +
               ", Daily Rate: $" + String.format("%.2f", dailyRate) +
               ", Available: " + (isAvailable ? "Yes" : "No");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}