package com.shuaib.oopcw.models;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.synchronized_ticketpool.TicketPool;

public class Vendor implements Runnable {
    private final Logger logger;
    private final Configuration config;
    private final TicketPool ticketPool;

    private final int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval;

    private boolean runStatus;

    private int ticketsAddable; //Only applicable when ticketsPerRelease will exceed maximum producable tickets amount

    public Vendor(int ticketsPerRelease, int releaseInterval) {
        this.logger = LogManager.getLogger("GLOBAL");
        this.config = Configuration.getInstance();
        this.ticketPool = TicketPool.getInstance();

        this.vendorId = new Random().nextInt(10000);
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;

        this.runStatus = true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                config.vendorWaitTillRunning(this);
                
                if (ticketPool.getTicketCount() >= config.getTotalTickets()){
                    logger.warn("Total Tickets producable reached.");
                }

                else if (ticketPool.getTicketCount() + this.ticketsPerRelease >= config.getTotalTickets()) {
                    this.ticketsAddable = config.getTotalTickets() - ticketPool.getTicketCount();
                    logger.warn("Cannot add tickets according to vendor {} tickets per release rate because total tickets producable will be exceeded./nAdding {} tickets instead.", this.vendorId, this.ticketsAddable);
                    ticketPool.addTicket(this.ticketsAddable, this);
                    logger.info("Vendor {} released {} tickets.", this.vendorId, this.ticketsAddable);
                    this.ticketsAddable = 0; //reset variable
                }

                else {
                    ticketPool.addTicket(this.ticketsPerRelease, this); 
                    logger.info("Vendor {} released {} tickets.", this.vendorId, this.ticketsPerRelease);
                }
                Thread.sleep(config.getTicketReleaseRate());
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

    public boolean getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
        if (config.getRunStatus() && runStatus) {
            config.resumeRunning();
        }
    }

    public void removeVendor() {
        Thread.currentThread().interrupt();
    }
}
