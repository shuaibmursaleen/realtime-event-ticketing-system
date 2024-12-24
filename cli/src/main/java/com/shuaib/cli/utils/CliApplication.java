package com.shuaib.cli.utils;

import com.shuaib.cli.models.Configuration;
import com.shuaib.cli.models.Customer;
import com.shuaib.cli.models.Ticket;
import com.shuaib.cli.models.Vendor;

import java.util.*;

public class CliApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Client client = new Client("http://localhost:8080");
    private static int input;
    public static void main(String[] args) {
        System.out.println("Welcome to the Ticket Management System.");      //Welcome message
        while (true) {
            int option;
            try {
                System.out.print("*".repeat(50) + "\n*"
                        + " ".repeat(18)
                        + "MENU OPTIONS"
                        + " ".repeat(18)
                        + "*\n" + "*".repeat(50)
                        + "\n     1) Set Configuration\n     2) Add Vendor\n     3) Add Customer\n     "
                        + "4) View Vendors\n     5) View Customers\n     6) View Tickets\n     0) Quit\n"
                        + "*".repeat(50) + "\nPlease select an option: ");
                option = scanner.nextInt();
            } catch (InputMismatchException e) {    //catching input exceptions
                System.out.println("Invalid input!");
                scanner.nextLine();   //clear input buffer
                continue;
            }
            switch (option) {   //switch block to select option
                case 1:
                    updateConfiguration();
                    break;
                case 2:
                    addVendor();
                    break;
                case 3:
                    addCustomer();
                    break;
                case 4:
                    getVendors();
                    break;
                case 5:
                    getCustomers();
                    break;
                case 6:
                    getTickets();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void updateConfiguration() {
        HashMap<String, Object> map = new HashMap<>();
        System.out.println("Setup Configuration");
        getInputs("Total Tickets");
        int totalTickets = input;
        map.put("total_tickets", totalTickets);

        getInputs("Ticket Release Rate");
        int ticketReleaseRate = input;
        map.put("ticket_release_rate", ticketReleaseRate);

        getInputs("Customer Retrieval Rate");
        int customerRetrievalRate = input;
        map.put("customer_retrieval_rate", customerRetrievalRate);

        getInputs("Max Ticket Capacity");
        int maxTicketCapacity = input;
        map.put("max_ticket_capacity", maxTicketCapacity);

        client.post("/configuration", map, Configuration.class);
    }

    public static Vendor addVendor() {
        HashMap<String, Object> map = new HashMap<>();

        getInputs("Tickets Per Release");
        int ticketsPerRelease = input;
        map.put("tickets_per_release", ticketsPerRelease);
                ;
        getInputs("Release Interval");
        int ticketReleaseRate = input;
        map.put("release_interval", ticketReleaseRate);

        Vendor vendor = client.post("/vendors", map, Vendor.class);

        System.out.println("Vendor ID: " + vendor.getVendorId());
        System.out.println("Tickets Per Release: " + vendor.getTicketsPerRelease());
        System.out.println("Release Interval: " + vendor.getReleaseInterval());
        System.out.println("Vendor Status: " + vendor.getRunStatus());
        System.out.println();

        return vendor;
    }

    public static Customer addCustomer() {
        HashMap<String, Object> map = new HashMap<>();

        getInputs("Retrieval Interval");
        int retrievalInterval = input;
        map.put("retrieval_interval", retrievalInterval);

        Customer customer = client.post("/customers", map, Customer.class);

        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Retrieval Interval: " + customer.getRetrievalInterval());
        System.out.println("Customer Status: " + customer.getRunStatus());
        System.out.println();

        return customer;
    }

    public static List<Vendor> getVendors() {
        Vendor[] list = client.get("/vendors", Vendor[].class);
        for (Vendor vendor : list) {
            System.out.println("Vendor ID: " + vendor.getVendorId());
            System.out.println("Tickets Per Release: " + vendor.getTicketsPerRelease());
            System.out.println("Release Interval: " + vendor.getReleaseInterval());
            System.out.println("Vendor Status: " + vendor.getRunStatus());
            System.out.println();
        }
        return Arrays.asList(list);
    }

    public static List<Customer> getCustomers() {
        Customer[] list = client.get("/customers", Customer[].class);
        for (Customer customer : list) {
            System.out.println("Customer ID: " + customer.getCustomerId());
            System.out.println("Retrieval Interval: " + customer.getRetrievalInterval());
            System.out.println("Customer Status: " + customer.getRunStatus());
            System.out.println();
        }
        return Arrays.asList(list);
    }

    public static List<Ticket> getTickets() {
        Ticket[] list = client.get("/tickets", Ticket[].class);
        for (Ticket ticket : list) {
            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Vendor ID: " + ticket.getVendorId());
            System.out.println();
        }
        return Arrays.asList(list);
    }

    private static void getInputs(String message) {
        while (true) {
            try {
                System.out.print("Enter " + message + ": ");
                input = scanner.nextInt();
                if (input < 0) {
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
