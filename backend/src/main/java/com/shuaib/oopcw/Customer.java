package com.shuaib.oopcw;

import java.util.Random;

public class Customer implements Runnable {
    private final int customerId;
    private int retrievalInterval;

    private boolean vipCustomer;

    public Customer(int retrievalInterval, boolean vipCustomer) {
        this.customerId = new Random().nextInt(10000);
        this.retrievalInterval = retrievalInterval;
        this.vipCustomer = vipCustomer;
    }

    @Override
    public void run() {}

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
