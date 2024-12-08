package com.shuaib.oopcw.core;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;

public class Vendor implements Runnable {
    private static final Logger logger = LogManager.getLogger("GLOBAL");
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
                    logger.warn("Total Tickets producable reached.");
                }

                else if (TicketPool.getInstance().getTotalTickets() + this.ticketsPerRelease >= Configuration.getInstance().getTotalTickets()) {
                    this.ticketsAddable = Configuration.getInstance().getTotalTickets() - TicketPool.getInstance().getTotalTickets();
                    logger.warn("Cannot add tickets according to vendor {} tickets per release rate because total tickets producable will be exceeded./nAdding {} tickets instead.", this.vendorId, this.ticketsAddable);
                    TicketPool.getInstance().addTicket(this.ticketsAddable, this.vendorId);
                    logger.info("Vendor {} released {} tickets.", this.vendorId, this.ticketsAddable);
                    this.ticketsAddable = 0; //reset variable
                }

                else {
                    TicketPool.getInstance().addTicket(this.ticketsPerRelease, this.vendorId); 
                    logger.info("Vendor {} released {} tickets.", this.vendorId, this.ticketsPerRelease);
                }
                System.out.println(TicketPool.getInstance().getTotalTickets());
                Thread.sleep(this.releaseInterval);
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupted");
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
