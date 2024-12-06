package com.shuaib.oopcw.backend_api;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.core.Customer;
import com.shuaib.oopcw.core.Ticket;
import com.shuaib.oopcw.core.TicketPool;
import com.shuaib.oopcw.core.Vendor;

@SpringBootApplication
@CrossOrigin
@RestController
public class OopcwApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopcwApplication.class, args);
	}

	public HashMap<Integer, Thread> vendorThreads= new HashMap<Integer, Thread>();
	public HashMap<Integer, Thread> customerThreads = new HashMap<Integer, Thread>();

	public HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	public HashMap<Integer, Vendor> vendors = new HashMap<Integer, Vendor>();

	public List<String> logs = new ArrayList<String>();

	@GetMapping("/configuration")
	public Object getConfiguration() {
		Configuration.getInstance().loadConfigJson("config.json");
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("total_tickets", Configuration.getInstance().getTotalTickets());
		response.put("ticket_release_rate", Configuration.getInstance().getTicketReleaseRate());
		response.put("customer_retrieval_rate", Configuration.getInstance().getCustomerRetrievalRate());
		response.put("max_ticket_capacity", Configuration.getInstance().getMaxTicketCapacity());
		response.put("release_interval", Configuration.getInstance().getReleaseInterval());
		return response;
	}

	@PostMapping("/configuration")
	public HashMap<String, Object> setConfiguration(@RequestBody HashMap<String, Object> body) {
		if (body.containsKey("load_config")) Configuration.getInstance().loadConfigJson("config.json");
		if (body.containsKey("status")) Configuration.getInstance().setRunStatus((boolean)body.get("status"));
		if (body.containsKey("total_tickets")) Configuration.getInstance().setTotalTickets((int)body.get("total_tickets"));
		if (body.containsKey("ticket_release_rate")) Configuration.getInstance().setTicketReleaseRate((int)body.get("ticket_release_rate"));
		if (body.containsKey("customer_retrieval_rate")) Configuration.getInstance().setCustomerRetrievalRate((int)body.get("customer_retrieval_rate"));
		if (body.containsKey("max_ticket_capacity")) Configuration.getInstance().setMaxTicketCapacity((int)body.get("max_ticket_capacity"));
		if (body.containsKey("release_interval")) Configuration.getInstance().setReleaseInterval((int)body.get("release_interval"));
		Configuration.getInstance().saveConfigJson("config.json");
		return body;
	}

	@GetMapping("/tickets")
	public List<Ticket> getTickets() {
		return TicketPool.getInstance().getTickets();
	}

	@GetMapping("/customers")
	public HashMap<Integer, Customer> getCustomers() {
		return customers;
	}

	@PostMapping("/customers")
	public Customer createCustomer(@RequestBody HashMap<String, Object> body) {
		int retrievalInterval = body.containsKey("retrieval_interval") ? (int) body.get("retrieval_interval") : Configuration.getInstance().getCustomerRetrievalRate();
		boolean vipCustomer = body.containsKey("vip_customer") ? (boolean) body.get("vip_customer") : false;
		Customer customer = new Customer(retrievalInterval, vipCustomer);
		customers.put(customer.getCustomerId(), customer);
		Thread thread = new Thread(customer,"Customer" + String.valueOf(customer.getCustomerId()));
		customerThreads.put(customer.getCustomerId(), thread);
		thread.start();
		return customer;
	}

	@GetMapping("/vendors")
	public HashMap<Integer, Vendor> getVendors() {
		return vendors;
	}

	@PostMapping("/vendors")
	public Vendor createVendor(@RequestBody HashMap<String, Object> body) {
		int ticketsPerRelease = body.containsKey("tickets_per_release") ? (int) body.get("tickets_per_release") : Configuration.getInstance().getTicketReleaseRate();
		int releaseInterval = body.containsKey("release_interval") ? (int) body.get("release_interval") : Configuration.getInstance().getReleaseInterval();
		Vendor vendor = new Vendor(ticketsPerRelease, releaseInterval);
		vendors.put(vendor.getVendorId(), vendor);
		Thread thread = new Thread(vendor,"Vendor:" + String.valueOf(vendor.getVendorId()));
		vendorThreads.put(vendor.getVendorId(), thread);
		vendorThreads.get(vendor.getVendorId()).start();
		return vendor;
	}

	@PostMapping("/vendors/toggle")
	public void toggleVendor(@RequestBody HashMap<String, Object> body) {
		if ((boolean) body.get("vendor_toggle")) {
			if (!vendorThreads.get((int) body.get("vendor_id")).isAlive()) {
				vendorThreads.get((int)body.get("vendor_id")).start();
			}
		}
		else {
			vendorThreads.get((int)body.get("vendor_id")).interrupt();
			vendorThreads.replace((int)body.get("vendor_id"), new Thread(vendors.get((int) body.get("vendor_id")), "Vendor: " + body.get("vendor_id")));
		}
	}

	@PostMapping("/customers/toggle")
	public void toggleCustomer(@RequestBody HashMap<String, Object> body) {
		if ((boolean) body.get("customer_toggle")) {
			if (!customerThreads.get((int) body.get("customer_id")).isAlive()) {
				customerThreads.get((int)body.get("customer_id")).start();
			}
		}
		else {
			customerThreads.get((int)body.get("customer_id")).interrupt();
			customerThreads.replace((int)body.get("customer_id"), new Thread(vendors.get((int) body.get("customer_id")), "Customer: " + body.get("customer_id")));
		}
	}
}