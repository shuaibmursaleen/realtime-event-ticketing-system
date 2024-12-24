package com.shuaib.oopcw.models;

import java.util.Random;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.eventstreams.LogStream;
import com.shuaib.oopcw.synchronized_ticketpool.TicketPool;

public class Vendor implements Runnable {
    private final Configuration config;
    private final TicketPool ticketPool;

    private final int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval;

    private boolean runStatus;

    public Vendor(int ticketsPerRelease, int releaseInterval) {
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
            while (true) {
                config.vendorWaitTillRunning(this);
                ticketPool.addTicket(this);
                LogStream.getInstance().addEvent(
                        String.format("Vendor %d released %d tickets.", this.vendorId, this.ticketsPerRelease));
                Thread.sleep(config.getTicketReleaseRate());
            }
        } catch (InterruptedException e) {
            LogStream.getInstance().addEvent(String.format("Vendor %d Interrupted", this.vendorId));
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
