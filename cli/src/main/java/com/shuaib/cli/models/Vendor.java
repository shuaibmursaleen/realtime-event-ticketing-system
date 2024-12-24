package com.shuaib.cli.models;

import java.util.Random;

public class Vendor {
    private final int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval;

    private boolean runStatus;

    public Vendor(int ticketsPerRelease, int releaseInterval) {
        this.vendorId = new Random().nextInt(1000);
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
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

    public boolean getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }

    public int getReleaseInterval() {
        return this.releaseInterval;
    }

    public void setReleaseInterval(int releaseInterval) {
        this.releaseInterval = releaseInterval;
    }
}
