package com.expensetracker;

import java.time.LocalDate;

public class Expense {

    private LocalDate date;
    private String category;
    private double amount;

    public Expense(LocalDate date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Date: " + date + " | Category: " + category + " | Amount: " + amount;
    }

    // Convert to CSV format
    public String toCSV() {
        return date + "," + category + "," + amount;
    }

    // Parse from CSV line
    public static Expense fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        LocalDate date = LocalDate.parse(parts[0]);
        String category = parts[1];
        double amount = Double.parseDouble(parts[2]);
        return new Expense(date, category, amount);
    }
}
