package com.shuaib.oopcw.models;

import java.util.Random;

public class Ticket {
    private final int ticketId;
    private final int vendorId;

    public Ticket(int vendorId) {
        this.ticketId = new Random().nextInt(10000);
        this.vendorId = vendorId;
    }

    public int getTicketId() {
        return this.ticketId;
    }

    public int getVendorId() {
        return this.vendorId;
    }
}
