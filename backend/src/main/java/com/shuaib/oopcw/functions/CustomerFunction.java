package com.shuaib.oopcw.functions;

import java.util.HashMap;
import java.util.Map;

import com.shuaib.oopcw.models.Customer;
public class CustomerFunction {
    private static CustomerFunction instance;
    private final Map<Integer, Customer> customers;
    private final Map<Integer, Thread> customerThreads;

    private CustomerFunction() {
	    this.customers = new HashMap<>();
        this.customerThreads = new HashMap<>();
    }

    public static CustomerFunction getInstance() {
        if (instance == null) {
            instance = new CustomerFunction();
        }
        return instance;
    }

    public Map<Integer, Customer> getCustomers() {
        return this.customers;
    }

    public void addCustomer(Customer customer) {
        this.customers.put(customer.getCustomerId(), customer);
    }

    public Map<Integer, Thread> getCustomerThreads() {
        return this.customerThreads;
    }

    public void addCustomerThread(int customerId, Thread thread) {
        this.customerThreads.put(customerId, thread);
    }

    public void setCustomerStatus(int customerId, boolean status) {
        this.customers.get(customerId).setRunStatus(status);
    }

    public void removeCustomer(int customerId) {
        this.customerThreads.get(customerId).interrupt();
        this.customerThreads.remove(customerId);
        this.customers.remove(customerId);
    }
}
