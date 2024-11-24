package com.shuaib.oopcw;

import java.util.Random;

public class Vendor implements Runnable {
    private final int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval;
    private int ticketsAddable; //Only applicable when ticketsPerRelease will exceed maximum producable tickets amount

    public Vendor(int ticketsPerRelease, int releaseInterval) {
        this.vendorId = new Random().nextInt(10000);
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                if (!Configuration.getInstance().getRunStatus()) {
                    continue;
                }
                
                if (TicketPool.getInstance().getTotalTickets() >= Configuration.getInstance().getTotalTickets()){
                    System.out.println("Total Tickets producable reached.");
                    System.out.println(Configuration.getInstance().getTotalTickets());
                }

                else if (TicketPool.getInstance().getTotalTickets() + this.ticketsPerRelease >= Configuration.getInstance().getTotalTickets()) {
                    this.ticketsAddable = Configuration.getInstance().getTotalTickets() - TicketPool.getInstance().getTotalTickets();
                    System.out.printf("Cannot add tickets according to vendor %d tickets per release rate because total tickets producable will be exceeded.%nAdding %d tickets instead.%n", this.vendorId, this.ticketsAddable);
                    TicketPool.getInstance().addTicket(this.ticketsAddable, this.vendorId);
                    System.out.printf("Vendor %d released %d tickets.%n", this.vendorId, this.ticketsAddable);
                    this.ticketsAddable = 0; //reset variable
                }

                else {
                    TicketPool.getInstance().addTicket(this.ticketsPerRelease, this.vendorId); 
                    System.out.printf("Vendor %d released %d tickets.%n", this.vendorId, this.ticketsPerRelease);
                }
                System.out.println(TicketPool.getInstance().getTotalTickets());
                Thread.sleep(this.releaseInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public int getVendorId() {
        return this.vendorId;
    }

    public int getTicketsPerRelease() {
        return this.ticketsPerRelease;
    }

    public void setTicketsPerRelease(int ticketsPerRelease) {
        this.ticketsPerRelease = ticketsPerRelease;
    }

    public int getReleaseInterval() {
        return this.releaseInterval;
    }

    public void setReleaseInterval(int releaseInterval) {
        this.releaseInterval = releaseInterval;
    }
}
