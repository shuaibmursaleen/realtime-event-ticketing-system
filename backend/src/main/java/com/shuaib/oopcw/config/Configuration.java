package com.shuaib.oopcw.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import com.google.gson.Gson;
import com.shuaib.oopcw.models.Customer;
import com.shuaib.oopcw.models.Vendor;
import com.shuaib.oopcw.synchronized_ticketpool.TicketPool;

public class Configuration {
    private static Configuration instance;

    private static final Gson gson = new Gson();

    private volatile boolean runStatus = true;
    
    private volatile int totalTickets = 10;
    private volatile int ticketReleaseRate = 2000;
    private volatile int customerRetrievalRate = 2000;
    private volatile int maxTicketCapacity = 5;
    
    private Configuration() {}

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public boolean loadConfigJson(String json) {
        try {
            instance = gson.fromJson(new FileReader(json),Configuration.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveConfigJson(String json) {
        try {
            FileWriter writer = new FileWriter(json);
            gson.toJson(instance, writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean getRunStatus() {
        return this.runStatus;
    }

    public synchronized void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
        if (runStatus) {
            resumeRunning();
        }
    }

    public int getTotalTickets() {
        return this.totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        if (this.totalTickets != totalTickets) {
            this.totalTickets = totalTickets;
            TicketPool.getInstance().resumePool();
        }
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return this.ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return this.customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return this.maxTicketCapacity;
    }

    public synchronized void setMaxTicketCapacity(int maxTicketCapacity) {
        if (this.maxTicketCapacity != maxTicketCapacity) {
            this.maxTicketCapacity = maxTicketCapacity;
            TicketPool.getInstance().resumePool();
        }      
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public synchronized void vendorWaitTillRunning(Vendor vendor) {
        try {
            while (!this.runStatus || !vendor.getRunStatus()) {
                wait();
            } 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void customerWaitTillRunning(Customer customer) {
        try {
            while (!this.runStatus || !customer.getRunStatus()) {
                wait();
                System.out.println("wait stopped");
                System.out.println(this.runStatus);
                System.out.println(customer.getRunStatus());
            } 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void resumeRunning() {
        notifyAll();
    }

}