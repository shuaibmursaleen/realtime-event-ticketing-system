package com.shuaib.oopcw;

import java.util.Random;

public class Ticket {
    private final int ticketId;

    public Ticket() {
        this.ticketId = new Random().nextInt(10000);
    }

    public int getTicketId() {
        return ticketId;
    }
}
