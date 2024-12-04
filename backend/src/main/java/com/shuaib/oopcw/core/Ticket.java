package com.shuaib.oopcw;

import java.util.Random;

public class Ticket {
    private final int vendorId;
    private final int ticketId;

    public Ticket(int vendorId) {
        this.ticketId = new Random().nextInt(10000);
        this.vendorId = vendorId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getVendorId() {
        return vendorId;
    }
}
