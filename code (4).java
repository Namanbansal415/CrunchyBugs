// CarRentalSystem.java
package com.carrental;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CarRentalSystem {
    private static RentalService rentalService = new RentalService();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    addCar();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    rentCar();
                    break;
                case 4:
                    returnCar();
                    break;
                case 5:
                    listAvailableCars();
                    break;
                case 6:
                    listAllCars();
                    break;
                case 7:
                    listAllCustomers();
                    break;
                case 8:
                    listActiveRentals();
                    break;
                case 9:
                    listRentalHistory();
                    break;
                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Car Rental System Menu ---");
        System.out.println("1. Add New Car");
        System.out.println("2. Add New Customer");
        System.out.println("3. Rent a Car");
        System.out.println("4. Return a Car");
        System.out.println("5. List Available Cars");
        System.out.println("6. List All Cars");
        System.out.println("7. List All Customers");
        System.out.println("8. List Active Rentals");
        System.out.println("9. View Rental History");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addCar() {
        System.out.println("\n--- Add New Car ---");
        try {
            System.out.print("Enter car make: ");
            String make = scanner.nextLine();
            System.out.print("Enter car model: ");
            String model = scanner.nextLine();
            System.out.print("Enter car year: ");
            int year = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter license plate: ");
            String licensePlate = scanner.nextLine();
            System.out.print("Enter daily rental rate: ");
            double dailyRate = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (make.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || year <= 1900 || dailyRate <= 0) {
                System.out.println("Invalid input. Please ensure all fields are filled correctly.");
                return;
            }

            Car car = new Car(make, model, year, licensePlate, dailyRate);
            rentalService.addCar(car);
            System.out.println("Car added successfully! ID: " + car.getId());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }

    private static void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        try {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();
            System.out.print("Enter customer contact info (phone/email): ");
            String contactInfo = scanner.nextLine();

            if (name.isEmpty() || contactInfo.isEmpty()) {
                System.out.println("Name and contact info cannot be empty.");
                return;
            }

            Customer customer = new Customer(name, contactInfo);
            rentalService.addCustomer(customer);
            System.out.println("Customer added successfully! ID: " + customer.getId());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void rentCar() {
        System.out.println("\n--- Rent a Car ---");
        listAvailableCars();
        List<Car> availableCars = rentalService.getAvailableCars();
        if (availableCars.isEmpty()) {
            System.out.println("No cars available for rent.");
            return;
        }

        try {
            System.out.print("Enter Car ID to rent: ");
            int carId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            listAllCustomers();
            System.out.print("Enter Customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter rental start date (YYYY-MM-DD) (default: today): ");
            String startDateStr = scanner.nextLine();
            LocalDate rentalDate = LocalDate.now();
            if (!startDateStr.isEmpty()) {
                try {
                    rentalDate = LocalDate.parse(startDateStr, dateFormatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Using today's date.");
                }
            }
            
            System.out.print("Enter expected return date (YYYY-MM-DD): ");
            String returnDateStr = scanner.nextLine();
            LocalDate expectedReturnDate;
            try {
                expectedReturnDate = LocalDate.parse(returnDateStr, dateFormatter);
                if (expectedReturnDate.isBefore(rentalDate) || expectedReturnDate.isEqual(rentalDate)) {
                    System.out.println("Return date must be after the rental date.");
                    return;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
                return;
            }

            rentalService.rentCar(carId, customerId, rentalDate, expectedReturnDate);

        } catch (InputMismatchException e) {
            System.out.println("Invalid ID format. Please enter a number.");
            scanner.nextLine(); // Clear buffer
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void returnCar() {
        System.out.println("\n--- Return a Car ---");
        listActiveRentals();
        List<Rental> activeRentals = rentalService.getActiveRentals();
        if (activeRentals.isEmpty()) {
            System.out.println("No cars currently rented out.");
            return;
        }
        try {
            System.out.print("Enter Rental ID to return: ");
            int rentalId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter actual return date (YYYY-MM-DD) (default: today): ");
            String returnDateStr = scanner.nextLine();
            LocalDate actualReturnDate = LocalDate.now();
             if (!returnDateStr.isEmpty()) {
                try {
                    actualReturnDate = LocalDate.parse(returnDateStr, dateFormatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Using today's date.");
                }
            }
            
            Rental rental = rentalService.findAnyRentalById(rentalId).orElse(null);
            if (rental != null && actualReturnDate.isBefore(rental.getRentalDate())) {
                System.out.println("Return date cannot be before the rental date.");
                return;
            }

            rentalService.returnCar(rentalId, actualReturnDate);

        } catch (InputMismatchException e) {
            System.out.println("Invalid ID format. Please enter a number.");
            scanner.nextLine(); // Clear buffer
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void listAvailableCars() {
        System.out.println("\n--- Available Cars ---");
        List<Car> availableCars = rentalService.getAvailableCars();
        if (availableCars.isEmpty()) {
            System.out.println("No cars currently available.");
        } else {
            availableCars.forEach(System.out::println);
        }
    }

    private static void listAllCars() {
        System.out.println("\n--- All Cars in Fleet ---");
        List<Car> allCars = rentalService.getAllCars();
        if (allCars.isEmpty()) {
            System.out.println("No cars in the system yet.");
        } else {
            allCars.forEach(System.out::println);
        }
    }

    private static void listAllCustomers() {
        System.out.println("\n--- All Customers ---");
        List<Customer> allCustomers = rentalService.getAllCustomers();
        if (allCustomers.isEmpty()) {
            System.out.println("No customers registered yet.");
        } else {
            allCustomers.forEach(System.out::println);
        }
    }

    private static void listActiveRentals() {
        System.out.println("\n--- Active Rentals ---");
        List<Rental> activeRentals = rentalService.getActiveRentals();
        if (activeRentals.isEmpty()) {
            System.out.println("No cars are currently rented out.");
        } else {
            activeRentals.forEach(rental -> {
                System.out.println("--------------------");
                System.out.println(rental);
            });
            System.out.println("--------------------");
        }
    }
    
    private static void listRentalHistory() {
        System.out.println("\n--- Rental History (Completed Rentals) ---");
        List<Rental> rentalHistory = rentalService.getRentalHistory();
        if (rentalHistory.isEmpty()) {
            System.out.println("No completed rentals in history.");
        } else {
            rentalHistory.forEach(rental -> {
                System.out.println("--------------------");
                System.out.println(rental);
            });
            System.out.println("--------------------");
        }
    }
}