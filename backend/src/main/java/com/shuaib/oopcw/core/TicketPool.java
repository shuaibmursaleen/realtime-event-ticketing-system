package com.shuaib.oopcw.core;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;

public class TicketPool {
    private static final Logger logger = LogManager.getLogger("GLOBAL");
    private static TicketPool instance;

    private List<Ticket> tickets = Collections.synchronizedList(new ArrayList<Ticket>());

    private int maxPoolSize = Configuration.getInstance().getMaxTicketCapacity();
    private int totalTickets;

    private TicketPool() {}

    public static TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public int getTotalTickets() {
        return this.totalTickets;
    }

    public synchronized void addTicket(int ticketsPerRelease, int vendorId) {
        try {
            totalTickets += ticketsPerRelease;
            for (int i = 0; i < ticketsPerRelease; i++) {
                Ticket ticket = new Ticket(vendorId);
                while (tickets.size() >= maxPoolSize) {
                    logger.warn("Pool is full. Ticket {} waiting to be added.", ticket.getTicketId());
                    wait();
                }
                Thread.sleep(1000);
                tickets.add(ticket);
                logger.info("Ticket {} was added to the pool, current Ticket count is {}.", ticket.getTicketId(), tickets.size());
            }
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void removeTicket() {
        try {
            while (tickets.isEmpty()) {
                logger.warn("No tickets available. Waiting to be removed.");
                wait();
            }
            Thread.sleep(2000);
            logger.info("Ticket {} was removed from the pool.", tickets.get(0).getTicketId());
            tickets.remove(0);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } 
    }
}
