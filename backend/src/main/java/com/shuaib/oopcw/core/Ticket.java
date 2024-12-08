package com.shuaib.oopcw.core;

import java.util.Random;

public class Ticket {
    private final int vendorId;
    private final int ticketId;
    private final int customerId = 0;

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

    public int getCustomerId() {
        return this.customerId;
    }
}
