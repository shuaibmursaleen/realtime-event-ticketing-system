package com.shuaib.oopcw.synchronized_ticketpool;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.models.Ticket;
import com.shuaib.oopcw.models.Vendor;

import java.util.ArrayList;
import java.util.Collections;

public class TicketPool {
    private static final Logger logger = LogManager.getLogger("GLOBAL");
    private static TicketPool instance;

    private List<Ticket> tickets;

    private volatile int totalTickets;
    private volatile int ticketCount;

    private TicketPool() {
        this.tickets = Collections.synchronizedList(new ArrayList<Ticket>());
        this.totalTickets = Configuration.getInstance().getMaxTicketCapacity();
    }

    public static TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public int getTicketCount() {
        return this.ticketCount;
    }

    public void resetTicketCount() {
        this.ticketCount = 0;
    }

    public synchronized void addTicket(int ticketsPerRelease, Vendor vendor) throws InterruptedException {
        try {
            ticketCount += ticketsPerRelease;
            for (int i = 0; i < ticketsPerRelease; i++) {
                Ticket ticket = new Ticket(vendor.getVendorId());
                while (tickets.size() >= totalTickets) {
                    logger.warn("Pool is full. Ticket {} waiting to be added.", ticket.getTicketId());
                    wait();
                }
                tickets.add(ticket);
                logger.info("Ticket {} was added to the pool, current Ticket count is {}.", ticket.getTicketId(), tickets.size());
                Thread.sleep(vendor.getReleaseInterval());
            }
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        }
    }

    public synchronized void removeTicket() throws InterruptedException {
        try {
            while (tickets.isEmpty()) {
                logger.warn("No tickets available. Waiting to be removed.");
                wait();
            }
            logger.info("Ticket {} was removed from the pool.", tickets.get(0).getTicketId());
            tickets.remove(0);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException(); 
        } 
    }
}
