package com.shuaib.oopcw.synchronized_ticketpool;

import java.util.List;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.logs.LogsHelper;
import com.shuaib.oopcw.models.Ticket;
import com.shuaib.oopcw.models.Vendor;

import java.util.ArrayList;
import java.util.Collections;

public class TicketPool {
    private static TicketPool instance;

    private List<Ticket> tickets;

    private volatile int ticketCount;

    private TicketPool() {
        this.tickets = Collections.synchronizedList(new ArrayList<Ticket>());
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

    public synchronized void addTicket(Vendor vendor) throws InterruptedException {
        try {
            Thread.sleep(500);
            for (int i = 0; i < vendor.getTicketsPerRelease(); i++) {
                Ticket ticket = new Ticket(vendor.getVendorId());
                while (this.ticketCount >= Configuration.getInstance().getTotalTickets()) {
                    LogsHelper.getInstance().addLog("Total producable tickets reached");
                    waitForSpace();
                }
                while (tickets.size() >= Configuration.getInstance().getMaxTicketCapacity()) {
                    LogsHelper.getInstance().addLog(String.format("Pool is full. Ticket %d waiting to be added.", ticket.getTicketId()));
                    waitForSpace();
                }
                tickets.add(ticket);
                LogsHelper.getInstance().addLog(String.format("Ticket %d was added to the pool, current Ticket count is %d.", ticket.getTicketId(), tickets.size()));
                Thread.sleep(vendor.getReleaseInterval());
                ticketCount++;
            }
            Thread.sleep(500);
            resumePool();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        }
    }

    public synchronized void removeTicket() throws InterruptedException {
        try {
            Thread.sleep(500);
            while (tickets.isEmpty()) {
                LogsHelper.getInstance().addLog("No tickets available. Waiting to be removed.");
                wait();
            }
            LogsHelper.getInstance().addLog(String.format("Ticket %d was removed from the pool.", tickets.get(0).getTicketId()));
            tickets.remove(0);
            Thread.sleep(500);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException(); 
        } 
    }

    public synchronized void waitForSpace() throws InterruptedException{
        wait();
    }

    public synchronized void resumePool() {
        notifyAll();
    }
}
