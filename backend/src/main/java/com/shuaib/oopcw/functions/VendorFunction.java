package com.shuaib.oopcw.functions;

import java.util.HashMap;
import java.util.Map;


import com.shuaib.oopcw.models.Vendor;

public class VendorFunction {
    private static VendorFunction instance;
    private final Map<Integer, Vendor> vendors;
    private final Map<Integer, Thread> vendorThreads;

    private VendorFunction() {
	    this.vendors = new HashMap<>();
        this.vendorThreads = new HashMap<>();
    }

    public static VendorFunction getInstance() {
        if (instance == null) {
            instance = new VendorFunction();
        }
        return instance;
    }

    public Map<Integer, Vendor> getVendors() {
        return this.vendors;
    }

    public void addVendor(Vendor vendor) {
        this.vendors.put(vendor.getVendorId(), vendor);
    }

    public Map<Integer, Thread> getVendorThreads() {
        return this.vendorThreads;
    }

    public void addVendorThread(int vendorId, Thread thread) {
        this.vendorThreads.put(vendorId, thread);
    }

    public void setVendorStatus(int vendorId, boolean status) {
        this.vendors.get(vendorId).setRunStatus(status);
    }

    public void removeVendor(int vendorId) {
        this.vendorThreads.get(vendorId).interrupt();
        this.vendorThreads.remove(vendorId);
        this.vendors.remove(vendorId);
    }
}
