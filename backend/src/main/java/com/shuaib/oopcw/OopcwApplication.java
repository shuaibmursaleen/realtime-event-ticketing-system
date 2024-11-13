package com.shuaib.oopcw;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OopcwApplication {
	public static void main(String[] args) {
		SpringApplication.run(OopcwApplication.class, args);
	}

	@GetMapping("/configuration")
	public Object getConfiguration() {
		// HashMap<String, Object> response = new HashMap<String, Object>();
		// response.put("status", Configuration.getInstance().getIsRunning());
		// return response;
		return Configuration.getInstance();
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

	@PostMapping("/customers")
	public Customer createCustomer(@RequestBody HashMap<String, Object> body) {
		int retrievalInterval = body.containsKey("retrieval_interval") ? (int) body.get("retrieval_interval") : Configuration.getInstance().getCustomerRetrievalRate();
		boolean vipCustomer = body.containsKey("vip_customer") ? (boolean) body.get("vip_customer") : false;
		Customer customer = new Customer(retrievalInterval, vipCustomer);
		Thread thread = new Thread(customer, String.valueOf(customer.getCustomerId()));
		thread.start();
		return customer;
	}

	@PostMapping("/vendors")
	public Vendor createVendor(@RequestBody HashMap<String, Object> body) {
		int ticketsPerRelease = body.containsKey("tickets_per_release") ? (int) body.get("tickets_per_release") : Configuration.getInstance().getTicketReleaseRate();
		int releaseInterval = body.containsKey("release_interval") ? (int) body.get("release_interval") : Configuration.getInstance().getReleaseInterval();
		Vendor vendor = new Vendor(ticketsPerRelease, releaseInterval);
		new Thread(vendor, String.valueOf(vendor.getVendorId())).start();
		return vendor;

	}
}