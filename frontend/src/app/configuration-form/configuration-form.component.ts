import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';

interface Configuration {
  total_tickets: number;
  ticket_release_rate: number;
  customer_retrieval_rate: number;
  max_ticket_capacity: number;
}

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './configuration-form.component.html',
  styles: []
})

export class ConfigurationFormComponent implements OnInit{
  config: Configuration;

  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketCapacity: number;

  constructor(private host:AppComponent) {
    this.config = {total_tickets: 0,
      ticket_release_rate: 0,
      customer_retrieval_rate: 0,
      max_ticket_capacity: 0
    };
    this.totalTickets = 0;
    this.ticketReleaseRate = 0;
    this.customerRetrievalRate = 0;
    this.maxTicketCapacity = 0;

  }


  async ngOnInit(): Promise<void> {
    this.getConfig();
  }

  async getConfig(): Promise<void> {
    try{
      const response = await this.host.client.get("/configuration");
      this.config = response.data;
    } catch(error) {
      this.config = {total_tickets: 0,
        ticket_release_rate: 0,
        customer_retrieval_rate: 0,
        max_ticket_capacity: 0
      };
    }
  }

  async setConfig(total_tickets: number, ticket_release_rate: number, customer_retrieval_rate: number, max_ticket_capacity: number): Promise<void> {
    console.log(total_tickets, ticket_release_rate, customer_retrieval_rate, max_ticket_capacity);
    this.host.client.post("/configuration", {
      "total_tickets": total_tickets,
      "ticket_release_rate": ticket_release_rate,
      "customer_retrieval_rate": customer_retrieval_rate,
      "max_ticket_capacity": max_ticket_capacity
    })
    this.getConfig();
  }
}
