package com.shuaib.oopcw.functions;

import java.util.HashMap;
import java.util.Map;


import com.shuaib.oopcw.models.Vendor;

public class VendorFunction {
    public static Map<Integer, Vendor> vendors;
    public static Map<Integer, Thread> vendorThreads;

    public VendorFunction() {
	    vendors = new HashMap<>();
        vendorThreads = new HashMap<>();
    }

    public Map<Integer, Vendor> getVendors() {
        return vendors;
    }

    public void addVendor(Vendor vendor) {
        vendors.put(vendor.getVendorId(), vendor);
    }

    public Map<Integer, Thread> getVendorThreads() {
        return vendorThreads;
    }

    public void addVendorThread(int vendorId, Thread thread) {
        vendorThreads.put(vendorId, thread);
    }

    public void setVendorStatus(int vendorId, boolean status) {
        vendors.get(vendorId).setRunStatus(status);
    }

    public void removeVendor(int vendorId) {
        vendorThreads.get(vendorId).interrupt();
        vendorThreads.remove(vendorId);
        vendors.remove(vendorId);
    }
}
