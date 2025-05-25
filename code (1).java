// Customer.java
package com.carrental;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int id;
    private String name;
    private String contactInfo; // e.g., phone number or email

    public Customer(String name, String contactInfo) {
        this.id = idGenerator.incrementAndGet();
        this.name = name;
        this.contactInfo = contactInfo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Customer ID: " + id +
               ", Name: '" + name + '\'' +
               ", Contact: '" + contactInfo + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}