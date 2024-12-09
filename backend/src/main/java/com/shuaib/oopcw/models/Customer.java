package com.shuaib.oopcw.models;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.synchronized_ticketpool.TicketPool;

public class Customer implements Runnable {
    private final Logger logger;

    private final int customerId;
    private int retrievalInterval;

    private boolean runStatus;

    public Customer(int retrievalInterval) {
        this.logger = LogManager.getLogger("GLOBAL");

        this.customerId = new Random().nextInt(10000);
        this.retrievalInterval = retrievalInterval;

        this.runStatus = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Configuration.getInstance().customerWaitTillRunning(this);
                TicketPool.getInstance().removeTicket();
                logger.info("Customer {} removed a ticket.", this.customerId);
                Thread.sleep(this.retrievalInterval);
                }          
        } catch (InterruptedException e) {
            logger.warn("Interrupted");
            Thread.currentThread().interrupt();
        }
    }
    
    public int getCustomerId() {
        return this.customerId;
    }

    public int getRetrievalInterval() {
        return this.retrievalInterval;
    }

    public void setRetrievalInterval(int retrievalInterval) {
        this.retrievalInterval = retrievalInterval;
    }

    public boolean getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
        if (Configuration.getInstance().getRunStatus() && runStatus) {
            Configuration.getInstance().resumeRunning();
        }
    }

    public void removeCustomer() {
        Thread.currentThread().interrupt();
    }
}
