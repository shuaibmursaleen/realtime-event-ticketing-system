package com.shuaib.oopcw.backend_api;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shuaib.oopcw.config.Configuration;
import com.shuaib.oopcw.core.Customer;
import com.shuaib.oopcw.core.Ticket;
import com.shuaib.oopcw.core.TicketPool;
import com.shuaib.oopcw.core.Vendor;

import com.github.artbits.jsqlite.*;

@SpringBootApplication
@CrossOrigin
@RestController
public class OopcwApplication {
	
	public static final Logger logger = LogManager.getLogger("GLOBAL");

	public HashMap<Integer, Thread> vendorThreads= new HashMap<Integer, Thread>();
	public HashMap<Integer, Thread> customerThreads = new HashMap<Integer, Thread>();

	public HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	public HashMap<Integer, Vendor> vendors = new HashMap<Integer, Vendor>();

	public static DB db = DB.connect("./src/main/resources/test.db");
	public static void main(String[] args) {
		SpringApplication.run(OopcwApplication.class, args);
	}

	@GetMapping("/configuration")
	public Object getConfiguration() {
		Configuration.getInstance().loadConfigJson("./src/main/resources/config.json");
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("total_tickets", Configuration.getInstance().getTotalTickets());
		response.put("ticket_release_rate", Configuration.getInstance().getTicketReleaseRate());
		response.put("customer_retrieval_rate", Configuration.getInstance().getCustomerRetrievalRate());
		response.put("max_ticket_capacity", Configuration.getInstance().getMaxTicketCapacity());
		return response;
	}

	@PostMapping("/configuration")
	public HashMap<String, Object> setConfiguration(@RequestBody HashMap<String, Object> body) {
		if (body.containsKey("load_config")) Configuration.getInstance().loadConfigJson("./src/main/resources/config.json");
		if (body.containsKey("status")) Configuration.getInstance().setRunStatus((boolean)body.get("status"));
		if (body.containsKey("total_tickets")) Configuration.getInstance().setTotalTickets((int)body.get("total_tickets"));
		if (body.containsKey("ticket_release_rate")) Configuration.getInstance().setTicketReleaseRate((int)body.get("ticket_release_rate"));
		if (body.containsKey("customer_retrieval_rate")) Configuration.getInstance().setCustomerRetrievalRate((int)body.get("customer_retrieval_rate"));
		if (body.containsKey("max_ticket_capacity")) Configuration.getInstance().setMaxTicketCapacity((int)body.get("max_ticket_capacity"));
		Configuration.getInstance().saveConfigJson("./src/main/resources/config.json");
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
	public boolean createCustomer(@RequestBody HashMap<String, Object> body) {
		int retrievalInterval;
		if (body.containsKey("retrieval_interval")) {
			retrievalInterval = (int) body.get("retrieval_interval");
		}
		else return false;

		boolean vipCustomer = body.containsKey("vip_customer") ? (boolean) body.get("vip_customer") : false;
		Customer customer = new Customer(retrievalInterval, vipCustomer);
		customers.put(customer.getCustomerId(), customer);
		Thread thread = new Thread(customer,"Customer" + String.valueOf(customer.getCustomerId()));
		customerThreads.put(customer.getCustomerId(), thread);
		thread.start();
		return true;
	}

	@GetMapping("/vendors")
	public HashMap<Integer, Vendor> getVendors() {
		return vendors;
	}

	@PostMapping("/vendors")
	public boolean createVendor(@RequestBody HashMap<String, Object> body) {
		int ticketsPerRelease;
		int releaseInterval;
		if (body.containsKey("tickets_per_release")) {
			ticketsPerRelease = (int) body.get("tickets_per_release");
		} 
		else return false;

		if (body.containsKey("release_interval")) {
			releaseInterval = (int) body.get("release_interval");
		}
		else return false;

		Vendor vendor = new Vendor(ticketsPerRelease, releaseInterval);
		vendors.put(vendor.getVendorId(), vendor);
		Thread thread = new Thread(vendor,"Vendor:" + String.valueOf(vendor.getVendorId()));
		vendorThreads.put(vendor.getVendorId(), thread);
		vendorThreads.get(vendor.getVendorId()).start();
		return true;
	}

	@PostMapping("/vendors/{vendorId}/toggle")
	public void toggleVendor(@PathVariable int vendorId, @RequestBody HashMap<String, Object> body) {
		if ((boolean) body.get("vendor_toggle")) {
			if (!vendorThreads.get(vendorId).isAlive()) {
				vendorThreads.get(vendorId).start();
			}
		}
		else {
			vendorThreads.get(vendorId).interrupt();
			vendorThreads.replace(vendorId, new Thread(vendors.get(vendorId), "Vendor: " + vendorId));
		}
	}

	@PostMapping("/customers/{customerId}/toggle")
	public void toggleCustomer(@PathVariable int customerId,@RequestBody HashMap<String, Object> body) {
		if ((boolean) body.get("customer_toggle")) {
			if (!customerThreads.get(customerId).isAlive()) {
				customerThreads.get(customerId).start();
			}
		}
		else {
			customerThreads.get(customerId).interrupt();
			customerThreads.replace(customerId, new Thread(customers.get(customerId), "Customer: " + customerId));
		}
	}
}