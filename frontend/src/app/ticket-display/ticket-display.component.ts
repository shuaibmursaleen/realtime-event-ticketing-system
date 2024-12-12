import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';

interface Ticket {
  vendorId: number;
  ticketId: number;
}

@Component({
  selector: 'app-ticket-display',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './ticket-display.component.html',
  styles: [],
})
export class TicketDisplayComponent implements OnInit {
  tickets: Ticket[];

  constructor(private host: AppComponent) {
    this.tickets = [];
  }

  async ngOnInit(): Promise<void> {
    setInterval(() => this.getTickets(), 1000);
  }

  async getTickets(): Promise<void> {
    try {
      const response = await this.host.client.get('/tickets');
      this.tickets = response.data;
    } catch (error) {
      this.tickets = [];
    }
  }
}
