package com.shuaib.oopcw.backend_api;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.shuaib.oopcw.config.Configuration;

import com.shuaib.oopcw.models.Customer;
import com.shuaib.oopcw.models.Vendor;

import com.shuaib.oopcw.functions.CustomerFunction;
import com.shuaib.oopcw.functions.VendorFunction;

import com.shuaib.oopcw.synchronized_ticketpool.TicketPool;

@SpringBootApplication
@CrossOrigin
@RestController
public class OopcwApplication {
	
	public static final Logger logger = LogManager.getLogger("GLOBAL");

	public static final VendorFunction vendorFunction = new VendorFunction();
	public static final CustomerFunction customerFunction = new CustomerFunction();

	public static final Configuration config = Configuration.getInstance();

	public static void main(String[] args) {
		SpringApplication.run(OopcwApplication.class, args);
	}

	@GetMapping("/configuration")
	public ResponseEntity<?> getConfiguration() {
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("total_tickets", config.getTotalTickets());
		response.put("ticket_release_rate", config.getTicketReleaseRate());
		response.put("customer_retrieval_rate", config.getCustomerRetrievalRate());
		response.put("max_ticket_capacity", config.getMaxTicketCapacity());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

    @PostMapping("/configuration")
	public ResponseEntity<?> setConfiguration(@RequestBody HashMap<String, Object> body) {
		if (body.containsKey("load_config")) config.loadConfigJson("./src/main/resources/config.json");
		if (body.containsKey("status")) config.setRunStatus((boolean)body.get("status"));
		if (body.containsKey("total_tickets")) config.setTotalTickets((int)body.get("total_tickets"));
		if (body.containsKey("ticket_release_rate")) config.setTicketReleaseRate((int)body.get("ticket_release_rate"));
		if (body.containsKey("customer_retrieval_rate")) config.setCustomerRetrievalRate((int)body.get("customer_retrieval_rate"));
		if (body.containsKey("max_ticket_capacity")) config.setMaxTicketCapacity((int)body.get("max_ticket_capacity"));
		config.saveConfigJson("./src/main/resources/config.json");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@GetMapping("/tickets")
	public ResponseEntity<?> getTickets() {
		return new ResponseEntity<>(TicketPool.getInstance().getTickets(), HttpStatus.OK);
	}

	@GetMapping("/vendors")
	public ResponseEntity<?> getVendors() {
		return new ResponseEntity<>(vendorFunction.getVendors(), HttpStatus.OK);
	}

    @PostMapping("/vendors")
	public ResponseEntity<?> createVendor(@RequestBody HashMap<String, Object> body) {
		int ticketsPerRelease;
		int releaseInterval;
		if (body.containsKey("tickets_per_release")) {
			ticketsPerRelease = (int) body.get("tickets_per_release");
		} 
		else return new ResponseEntity<>("Tickets Per Release is needed!", HttpStatus.BAD_REQUEST);

		if (body.containsKey("release_interval")) {
			releaseInterval = (int) body.get("release_interval");
		}
		else return new ResponseEntity<>("Release Interval is needed!", HttpStatus.BAD_REQUEST);

		Vendor vendor = new Vendor(ticketsPerRelease, releaseInterval);
		vendorFunction.addVendor(vendor);
		Thread thread = new Thread(vendor,"Vendor:" + String.valueOf(vendor.getVendorId()));
		vendorFunction.addVendorThread(vendor.getVendorId(), thread);
		thread.start();
		return new ResponseEntity<>(vendor, HttpStatus.OK);
	}

    @PatchMapping("/vendors/{vendorId}")
	public ResponseEntity<?> toggleVendor(@PathVariable int vendorId, @RequestBody HashMap<String, Object> body) {
		if (body.containsKey("status")) {
			if ((boolean) body.get("status")) {
				vendorFunction.setVendorStatus(vendorId, true);
				return new ResponseEntity<>("Vendor started", HttpStatus.OK);
			}
			else {
				vendorFunction.setVendorStatus(vendorId, false);
				return new ResponseEntity<>("Vendor stopped", HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>("status required!", HttpStatus.BAD_REQUEST);
		}
	}

    @DeleteMapping("/vendors/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable int vendorId) {
		if (vendorFunction.getVendors().containsKey(vendorId)) {
			vendorFunction.removeVendor(vendorId);
			return new ResponseEntity<>("Vendor removed successfully", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Vendor doesn't exist", HttpStatus.BAD_REQUEST);
		}

    }

	@GetMapping("/customers")
	public ResponseEntity<?> getCustomers() {
		return new ResponseEntity<>(customerFunction.getCustomers(), HttpStatus.OK);
	}

	@PostMapping("customers")
	public ResponseEntity<?> createCustomer(@RequestBody HashMap<String, Object> body) {
		int retrievalInterval;
		if (body.containsKey("retrieval_interval")) {
			retrievalInterval = (int) body.get("retrieval_interval");
		}
		else return new ResponseEntity<>("Retrieval Interval is required!", HttpStatus.BAD_REQUEST);

		Customer customer = new Customer(retrievalInterval);
		customerFunction.addCustomer(customer);
		Thread thread = new Thread(customer, "Customer:" + String.valueOf(customer.getCustomerId()));
		customerFunction.addCustomerThread(customer.getCustomerId(), thread);
		thread.start();
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	@PatchMapping("/customers/{customerId}")
	public ResponseEntity<?> toggleCustomer(@PathVariable int customerId, @RequestBody HashMap<String, Object> body) {
		if (body.containsKey("status")) {
			if ((boolean) body.get("status")) {
				customerFunction.setCustomerStatus(customerId, true);
				return new ResponseEntity<>("Customer started successfully", HttpStatus.OK);
			}
			else {
				customerFunction.setCustomerStatus(customerId, false);
				return new ResponseEntity<>("Customer stopped successfully", HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>("status required!", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/customers/{customerId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable int customerId) {
		if (customerFunction.getCustomers().containsKey(customerId)) {
			customerFunction.removeCustomer(customerId);
			return new ResponseEntity<>("Customer removed successfully", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Customer does not exist", HttpStatus.BAD_REQUEST);
		}
	}
}