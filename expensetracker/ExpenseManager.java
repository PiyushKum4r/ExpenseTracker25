package com.expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseManager {

    private List<Expense> expenses;
    private static final String FILE_NAME = "expenses.csv";

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadFromCSV();
    }

    // Add a new expense
    public void addExpense(LocalDate date, String category, double amount) {
        expenses.add(new Expense(date, category, amount));
        saveToCSV();
    }

    // View all expenses
    public List<Expense> getAllExpenses() {
        return expenses;
    }

    // Get expenses by category
    public List<Expense> getExpensesByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // Get total expenses
    public double getTotalExpenses() {
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // Get total expenses by category
    public double getTotalByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

 // Save expenses to CSV and upload to S3
    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.write(e.toCSV());
                writer.newLine();
            }
            System.out.println("💾 Expenses saved locally: " + FILE_NAME);

            // Upload to S3 after saving
            S3Uploader uploader = new S3Uploader(
                "AKIAX5W3",     //Replace with AWS access key
                "mzIEM6eUQ7XbdUq", // Replace with AWS secret key
                "us-east-1",             // Replace with your AWS region
                "setracker2025"        // Replace with your bucket name
            );
            uploader.uploadFile(FILE_NAME, FILE_NAME);
            uploader.close();

        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }


    // Load expenses from CSV
    private void loadFromCSV() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(Expense.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }
}

