package com.shuaib.cli.models;

import java.util.Random;

public class Customer {
    private final int customerId;
    private int retrievalInterval;

    private boolean runStatus;

    public Customer(int retrievalInterval) {
        this.customerId = new Random().nextInt(1000);
        this.retrievalInterval = retrievalInterval;
        this.runStatus = true;
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
    }
}
