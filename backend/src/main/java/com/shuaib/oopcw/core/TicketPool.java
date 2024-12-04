package com.shuaib.oopcw.core;

import java.util.List;

import com.shuaib.oopcw.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;

public class TicketPool {
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
                    System.out.printf("Pool is full. Ticket %d waiting to be added.%n", ticket.getTicketId());
                    wait();
                }
                Thread.sleep(1000);
                tickets.add(ticket);
                System.out.printf("Ticket %d was added to the pool, current Ticket count is %d.%n", ticket.getTicketId(), tickets.size());
            }
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } 
    }

    public synchronized void removeTicket() {
        try {
            while (tickets.isEmpty()) {
                System.out.println("No tickets available. Waiting to be removed.");
                wait();
            }
            Thread.sleep(2000);
            System.out.printf("Ticket %d was removed from the pool.%n", tickets.get(0).getTicketId());
            tickets.remove(0);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } 
    }
}
