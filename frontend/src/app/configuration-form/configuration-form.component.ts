import { Component, OnInit } from '@angular/core';
import { FormGroup, FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';

interface Configuration {
  total_tickets: number;
  ticket_release_rate: number;
  customer_retrieval_rate: number;
  max_ticket_capacity: number;
  status: boolean;
}

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './configuration-form.component.html',
  styles: [],
})
export class ConfigurationFormComponent implements OnInit {
  config: Configuration;

  totalTickets!: number;
  ticketReleaseRate!: number;
  customerRetrievalRate!: number;
  maxTicketCapacity!: number;

  constructor(private host: AppComponent) {
    this.config = {
      total_tickets: 0,
      ticket_release_rate: 0,
      customer_retrieval_rate: 0,
      max_ticket_capacity: 0,
      status: false,
    };
  }

  async ngOnInit(): Promise<void> {
    this.getConfig();
  }

  async getConfig(): Promise<void> {
    try {
      const response = await this.host.client.get('/configuration');
      this.config = response.data;
      console.log(this.config.total_tickets);
    } catch (error) {
      console.log(error);
    }
  }

  async setConfig(
    total_tickets: number,
    ticket_release_rate: number,
    customer_retrieval_rate: number,
    max_ticket_capacity: number
  ): Promise<void> {
    this.host.client.post('/configuration', {
      total_tickets: total_tickets,
      ticket_release_rate: ticket_release_rate,
      customer_retrieval_rate: customer_retrieval_rate,
      max_ticket_capacity: max_ticket_capacity,
    });
    this.getConfig();
  }
}
