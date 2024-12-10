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
  isTicketsLoading: boolean;

  constructor(private host: AppComponent) {
    this.tickets = [];
    this.isTicketsLoading = true;
  }

  async ngOnInit(): Promise<void> {
    this.loadTickets(true);
    setInterval(() => this.loadTickets(), 1000);
  }

  async loadTickets(initial: boolean = false): Promise<void> {
    try {
      if (initial) this.isTicketsLoading = true;
      const response = await this.host.client.get('/tickets');
      this.tickets = response.data;
      this.isTicketsLoading = false;
    } catch (error) {
      this.tickets = [];
    }
  }
}
