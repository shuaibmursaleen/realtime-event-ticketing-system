package com.shuaib.oopcw;

import java.util.Random;

public class Vendor implements Runnable {
    private final int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval;

    public Vendor(int ticketsPerRelease, int releaseInterval) {
        this.vendorId = new Random().nextInt(10000);
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {}

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
