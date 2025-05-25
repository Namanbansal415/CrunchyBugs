// Rental.java
package com.carrental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Rental {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int id;
    private Car car;
    private Customer customer;
    private LocalDate rentalDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double totalCost;
    private boolean isReturned;

    public Rental(Car car, Customer customer, LocalDate rentalDate, LocalDate expectedReturnDate) {
        this.id = idGenerator.incrementAndGet();
        this.car = car;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = null; // Not returned yet
        this.isReturned = false;
        this.totalCost = 0; // Calculated upon return or based on expected duration
        calculateInitialCost();
    }

    // Getters
    public int getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isReturned() {
        return isReturned;
    }

    // Setters
    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.isReturned = true;
        calculateFinalCost();
    }
    
    private void calculateInitialCost() {
        long days = ChronoUnit.DAYS.between(rentalDate, expectedReturnDate);
        if (days <= 0) days = 1; // Minimum 1 day rental
        this.totalCost = days * car.getDailyRate();
    }

    private void calculateFinalCost() {
        if (actualReturnDate == null) return; // Not returned yet

        long rentedDays = ChronoUnit.DAYS.between(rentalDate, actualReturnDate);
        if (rentedDays <= 0) rentedDays = 1; // Minimum 1 day rental

        this.totalCost = rentedDays * car.getDailyRate();

        // Optional: Add late fee logic
        if (actualReturnDate.isAfter(expectedReturnDate)) {
            long overdueDays = ChronoUnit.DAYS.between(expectedReturnDate, actualReturnDate);
            // Example late fee: 50% extra on daily rate for overdue days
            this.totalCost += overdueDays * car.getDailyRate() * 0.5; 
        }
    }

    @Override
    public String toString() {
        return "Rental ID: " + id +
               "\n  Car: [" + car.getMake() + " " + car.getModel() + " (ID: " + car.getId() + ")]" +
               "\n  Customer: [" + customer.getName() + " (ID: " + customer.getId() + ")]" +
               "\n  Rental Date: " + rentalDate +
               "\n  Expected Return Date: " + expectedReturnDate +
               (isReturned ? "\n  Actual Return Date: " + actualReturnDate : "\n  Status: Currently Rented") +
               "\n  Total Cost: $" + String.format("%.2f", totalCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return id == rental.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}