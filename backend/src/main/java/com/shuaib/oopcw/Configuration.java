package com.shuaib.oopcw;

public class Configuration {
    private static Configuration instance;

    private boolean runStatus = true;
    
    private int totalTickets = 10;
    private int ticketReleaseRate = 2;
    private int customerRetrievalRate = 2;
    private int maxTicketCapacity = 5;
    private int releaseInterval = 4000;
    
    private Configuration() {}

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public boolean getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }

    public int getTotalTickets() {
        return this.totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
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

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getReleaseInterval() {
        return this.releaseInterval;
    }

    public void setReleaseInterval(int releaseInterval) {
        this.releaseInterval = releaseInterval;
    }
}
