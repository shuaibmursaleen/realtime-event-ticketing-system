package com.shuaib.oopcw.functions;

import java.util.HashMap;
import java.util.Map;

import com.shuaib.oopcw.models.Customer;
public class CustomerFunction {
    private final Map<Integer, Customer> customers;
    private final Map<Integer, Thread> customerThreads;

    public CustomerFunction() {
	    this.customers = new HashMap<>();
        this.customerThreads = new HashMap<>();
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Map<Integer, Thread> getCustomerThreads() {
        return customerThreads;
    }

    public void addCustomerThread(int customerId, Thread thread) {
        customerThreads.put(customerId, thread);
    }

    public void setCustomerStatus(int customerId, boolean status) {
        customers.get(customerId).setRunStatus(status);
    }

    public void removeCustomer(int customerId) {
        customerThreads.get(customerId).interrupt();
        customerThreads.remove(customerId);
        customers.remove(customerId);
    }
}
