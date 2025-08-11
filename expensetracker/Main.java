package com.expensetracker;

import java.time.LocalDate;
import java.util.Scanner;

// AWS imports
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class Main {
    public static void main(String[] args) {

        // ==== AWS S3 setup ====
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
            "YOUR_ACCESS_KEY_ID",      // replace with your access key
            "YOUR_SECRET_ACCESS_KEY"   // replace with your secret key
        );

        S3Client s3 = S3Client.builder()
            .region(Region.of("ap-south-1")) // change if your bucket is in another region
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();

        // ==== Your existing expense tracker ====
        ExpenseManager manager = new ExpenseManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nExpense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Expenses by Category");
            System.out.println("4. View Total");
            System.out.println("5. View Total by Category");
            System.out.println("6. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    LocalDate date = LocalDate.now();
                    manager.addExpense(date, category, amount);
                    System.out.println("Expense added!");
                    break;
                case 2:
                    manager.getAllExpenses().forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Enter category: ");
                    String cat = sc.nextLine();
                    manager.getExpensesByCategory(cat).forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("Total: " + manager.getTotalExpenses());
                    break;
                case 5:
                    System.out.print("Enter category: ");
                    String cat2 = sc.nextLine();
                    System.out.println("Total in category: " + manager.getTotalByCategory(cat2));
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        
    }
}
