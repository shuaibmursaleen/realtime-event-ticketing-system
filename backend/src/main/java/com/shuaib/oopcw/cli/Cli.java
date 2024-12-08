package com.shuaib.oopcw.cli;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.shuaib.oopcw.config.Configuration;

public class Cli {
    public static int input;
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        getInputs("Total Tickets");
        Configuration.getInstance().setTotalTickets(input);

        getInputs("Ticket Release Rate");
        Configuration.getInstance().setTicketReleaseRate(input);

        getInputs("Customer Retrieval Rate");
        Configuration.getInstance().setCustomerRetrievalRate(input);
        
        getInputs("Max Ticket Capacity");
        Configuration.getInstance().setMaxTicketCapacity(input);

        boolean saveConfig = Configuration.getInstance().saveConfigJson("./src/main/resources/config.json");

        if (saveConfig == true) {
            System.out.println("Configuration saved succesfully!");
        }
        else {
            System.out.println("Configuration failed to save. Please close 'config.json' if open and try again");
        }
        scanner.close();
    }

    private static void getInputs(String message) {
        while(true) {
            try {
                System.out.print("Enter " + message + ": ");
                input = scanner.nextInt();
                if (input<0) {
                        System.out.println("Negative values are not valid. Please enter a valid amount.");
                        continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }
    }
}