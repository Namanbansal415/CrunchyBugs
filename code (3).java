// RentalService.java
package com.carrental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RentalService {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public RentalService() {
        this.cars = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
        // Add some sample data
        addSampleData();
    }
    
    private void addSampleData() {
        // Sample Cars
        addCar(new Car("Toyota", "Camry", 2022, "ABC123", 50.00));
        addCar(new Car("Honda", "Civic", 2021, "XYZ789", 45.00));
        addCar(new Car("Ford", "Mustang", 2023, "FGH456", 75.00));
        addCar(new Car("BMW", "X5", 2022, "BMW001", 120.00));

        // Sample Customers
        addCustomer(new Customer("Alice Smith", "alice@example.com"));
        addCustomer(new Customer("Bob Johnson", "555-1234"));
    }


    // Car Management
    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars); // Return a copy
    }

    public List<Car> getAvailableCars() {
        return cars.stream()
                   .filter(Car::isAvailable)
                   .collect(Collectors.toList());
    }

    public Optional<Car> findCarById(int carId) {
        return cars.stream()
                   .filter(car -> car.getId() == carId)
                   .findFirst();
    }

    // Customer Management
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers); // Return a copy
    }

    public Optional<Customer> findCustomerById(int customerId) {
        return customers.stream()
                       .filter(customer -> customer.getId() == customerId)
                       .findFirst();
    }

    // Rental Management
    public boolean rentCar(int carId, int customerId, LocalDate rentalDate, LocalDate expectedReturnDate) {
        Optional<Car> carOpt = findCarById(carId);
        Optional<Customer> customerOpt = findCustomerById(customerId);

        if (carOpt.isPresent() && customerOpt.isPresent()) {
            Car car = carOpt.get();
            if (car.isAvailable()) {
                Customer customer = customerOpt.get();
                Rental rental = new Rental(car, customer, rentalDate, expectedReturnDate);
                rentals.add(rental);
                car.setAvailable(false);
                System.out.println("Car rented successfully! Rental ID: " + rental.getId());
                return true;
            } else {
                System.out.println("Error: Car (ID: " + carId + ") is not available for rent.");
                return false;
            }
        } else {
            if (!carOpt.isPresent()) {
                System.out.println("Error: Car with ID " + carId + " not found.");
            }
            if (!customerOpt.isPresent()) {
                System.out.println("Error: Customer with ID " + customerId + " not found.");
            }
            return false;
        }
    }

    public boolean returnCar(int rentalId, LocalDate actualReturnDate) {
        Optional<Rental> rentalOpt = findRentalById(rentalId);

        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            if (!rental.isReturned()) {
                rental.getCar().setAvailable(true);
                rental.setActualReturnDate(actualReturnDate);
                System.out.println("Car returned successfully. Final cost: $" + String.format("%.2f", rental.getTotalCost()));
                return true;
            } else {
                System.out.println("Error: This rental (ID: " + rentalId + ") has already been returned.");
                return false;
            }
        } else {
            System.out.println("Error: Rental with ID " + rentalId + " not found.");
            return false;
        }
    }

    public Optional<Rental> findRentalById(int rentalId) {
        return rentals.stream()
                      .filter(rental -> rental.getId() == rentalId && !rental.isReturned())
                      .findFirst();
    }
    
    public Optional<Rental> findAnyRentalById(int rentalId) { // Finds active or returned
        return rentals.stream()
                      .filter(rental -> rental.getId() == rentalId)
                      .findFirst();
    }


    public List<Rental> getAllRentals() {
        return new ArrayList<>(rentals);
    }

    public List<Rental> getActiveRentals() {
        return rentals.stream()
                      .filter(rental -> !rental.isReturned())
                      .collect(Collectors.toList());
    }

    public List<Rental> getRentalHistory() {
         return rentals.stream()
                      .filter(Rental::isReturned)
                      .collect(Collectors.toList());
    }
}