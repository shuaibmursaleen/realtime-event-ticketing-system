# Application Documentation

## Overview

This application is a system designed to manage vendors, customers, and ticketing operations. It allows users to update configurations, add and manage vendors and customers, view tickets, and access system logs. The application contains both a frontend built with Angular and a backend API, and also includes a command-line interface (CLI) for performing operations.

### Frontend Components

The frontend Angular application includes the following components:

- **Configuration Form**: To update system configurations.
- **Vendor Display**: For CRUD (Create, Read, Update, Delete) operations on vendors.
- **Customer Display**: For CRUD operations on customers.
- **Ticket Display**: To view tickets in the ticket pool.
- **Log Display**: To display system logs in real time.

### CLI Operations

The application also includes a CLI for the following operations:

- **Update Configuration**: Update the system configuration.
- **Add Vendor**: Add a new vendor.
- **View Vendor**: View existing vendors.
- **Add Customer**: Add a new customer.
- **View Customer**: View existing customers.
- **View Tickets**: View tickets in the ticket pool.

## Features

- **Configuration Management**: Easily update system configurations such as ticket release rate, customer retrieval rate, and max ticket capacity.
- **Vendor and Customer Management**: Add, view, update, and delete vendors and customers.
- **Ticket Pool**: Track available tickets and release them based on the system configuration.
- **Log Display**: Real-time log streaming for monitoring system events.
- **CLI Interface**: Perform key actions directly from the command line.

## Setup and Installation

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java (JDK 21 or higher)** (for running the backend API)
- **Maven** (for building the backend)
- **Node.js** (for the frontend Angular application)
- **Bun** (for package management in the frontend)

### Backend Setup (Spring Boot)

1. Clone the repository:
    ```bash
    git clone https://github.com/shuaibmursaleen/realtime-event-ticketing-system.git
    cd realtime-event-ticketing-system/backend
    ```

2. Build the backend application using Maven:
    ```bash
    mvn clean install
    ```

3. Run the backend application:
    ```bash
    mvn spring-boot:run
    ```

4. The backend should now be running at `http://localhost:8080`.

### Frontend Setup (Angular + Bun)

1. Open the frontend directory:
    ```bash
    cd realtime-event-ticketing-system/frontend
    ```

2. Install dependencies with Bun:
    ```bash
    bun install
    ```

3. Run the Angular application:
    ```bash
    bun start
    ```

4. Open your browser and go to `http://localhost:4200` to access the frontend.

## API Documentation

### Configuration Endpoints

- **GET /configuration**: Fetch current system configuration.
- **POST /configuration**: Update the system configuration (e.g., ticket release rate, max ticket capacity).

### Vendor Endpoints

- **GET /vendors**: Retrieve the list of vendors.
- **POST /vendors**: Create a new vendor.
- **PATCH /vendors/{vendorId}**: Update vendor status (start/stop).
- **DELETE /vendors/{vendorId}**: Remove a vendor from the system.

### Customer Endpoints

- **GET /customers**: Retrieve the list of customers.
- **POST /customers**: Create a new customer.
- **PATCH /customers/{customerId}**: Update customer status (start/stop).
- **DELETE /customers/{customerId}**: Remove a customer from the system.

### Ticket Endpoints

- **GET /tickets**: View tickets in the ticket pool.

### Log Stream

- **GET /logs**: View system logs in real-time via an event stream.

## Usage

### Frontend Operations

- **Configuration Form**: Use the configuration form to update system settings such as the ticket release rate and max ticket capacity.
- **Vendor CRUD**: Add, update, and delete vendors using the vendor display. View vendor information, edit release rate, and start/stop vendors.
- **Customer CRUD**: Manage customers through the customer display. Add new customers, view their details, or stop/start customer operations.
- **Ticket Pool**: Monitor the ticket pool in the ticket display.
- **Log Display**: View real-time logs streaming in the log display.

### CLI Operations

You can also manage the system using the CLI. Run the following commands in your terminal:

- **Update Configuration**: Update the system configuration.
- **Add Vendor**: Add a new vendor.
- **View Vendor**: View the list of current vendors.
- **Add Customer**: Add a new customer.
- **View Customer**: View the list of current customers.
- **View Tickets**: View the current tickets in the pool.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Thank you for using this application!
