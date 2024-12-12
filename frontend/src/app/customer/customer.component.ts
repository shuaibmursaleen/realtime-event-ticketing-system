import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';

interface Customer {
  customerId: number;
  retrievalInterval: number;
  runStatus: boolean;
}

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [FormsModule, NgIf, NgFor],
  templateUrl: './customer.component.html',
  styles: [],
})
export class CustomerComponent implements OnInit {
  customers: Customer[];
  retrievalInterval!: number;
  status!: boolean;

  constructor(private host: AppComponent) {
    this.customers = [];
  }

  async ngOnInit(): Promise<void> {
    setInterval(() => this.getCustomers(), 1000);
  }

  async getCustomers(): Promise<void> {
    try {
      const response = await this.host.client.get('/customers');
      this.customers = response.data;
    } catch (error) {
      this.customers = [];
    }
  }

  async setCustomer(retrieval_interval: number): Promise<void> {
    this.host.client.post('/customers', {
      retrieval_interval: retrieval_interval,
    });
    this.getCustomers();
  }

  async toggleCustomer(customerId: number, status: boolean): Promise<void> {
    this.host.client.patch(`/customers/${customerId}`, {
      status: status,
    });
    this.getCustomers();
  }

  async removeCustomer(customerId: number): Promise<void> {
    this.host.client.delete(`/customers/${customerId}`);
    this.getCustomers();
  }
}
