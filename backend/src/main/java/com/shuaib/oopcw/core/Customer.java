package com.shuaib.oopcw.core;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuaib.oopcw.config.Configuration;

public class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger("GLOBAL");
    private final int customerId;
    private int retrievalInterval;

    private boolean vipCustomer;

    public Customer(int retrievalInterval, boolean vipCustomer) {
        this.customerId = new Random().nextInt(10000);
        this.retrievalInterval = retrievalInterval;
        this.vipCustomer = vipCustomer;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                if (!Configuration.getInstance().getRunStatus()) {
                    continue;
                }
                TicketPool.getInstance().removeTicket();
                if (this.vipCustomer) {
                    logger.info("VIP Customer {} removed a ticket.", this.customerId);
                }    
                else {
                    logger.info("Customer {} removed a ticket.", this.customerId);
                }
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

    public boolean getVipCustomer() {
        return this.vipCustomer;
    }

    public void setVipCustomer(boolean vipCustomer) {
        this.vipCustomer = vipCustomer;
    }
}
