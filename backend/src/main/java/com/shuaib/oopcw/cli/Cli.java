package com.shuaib.oopcw.cli;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.shuaib.oopcw.config.Configuration;

public class Cli {
    public static void main(String[] args) {
        int input;
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            try {
                System.out.print("Enter Total Ticket count: ");
                input = scanner.nextInt();
                if (input<0) {
                        System.out.println("Negative values are not valid. Please enter a valid amount.");
                        continue;
                }
                Configuration.getInstance().setTotalTickets(input);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }

        while(true) {
            try {
                System.out.print("Enter Ticket Release Rate: ");
                input = scanner.nextInt();
                if (input<0) {
                        System.out.println("Negative values are not valid. Please enter a valid amount.");
                        continue;
                }
                Configuration.getInstance().setTicketReleaseRate(input);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }

        while(true) {
            try {
                System.out.print("Enter Customer Retrieval Rate: ");
                input = scanner.nextInt();
                if (input<0) {
                        System.out.println("Negative values are not valid. Please enter a valid amount.");
                        continue;
                }
                Configuration.getInstance().setCustomerRetrievalRate(input);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }


        while(true) {
            try {
                System.out.print("Enter Ticketpol Max Ticket Capacity: ");
                input = scanner.nextInt();
                if (input<0) {
                        System.out.println("Negative values are not valid. Please enter a valid amount.");
                        continue;
                }
                Configuration.getInstance().setMaxTicketCapacity(input);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }


        while(true) {
            try {
                System.out.print("Enter Ticket Release Interval (min. 500): ");
                input = scanner.nextInt();
                if (input<500) {
                    System.out.println("Release Interval out of range. Please enter a valid interval");
                    continue;
                }
                Configuration.getInstance().setReleaseInterval(input);;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid amount.");
                scanner.next();
            }
        }

        boolean saveConfig = Configuration.getInstance().saveConfigJson("config.json");

        if (saveConfig == true) {
            System.out.println("Configuration saved succesfully! Run 'mvn spring-boot:run' to start the server");
        }
        else {
            System.out.println("Configuration failed to save. Please close 'config.json' if open and try again");
        }
        scanner.close();
    }
}