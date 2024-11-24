import { NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

interface Ticket {
  vendorId: number;
  ticketId: number;
}

@Component({
  selector: 'app-ticket-display',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './ticket-display.component.html',
  styles: []
})

export class TicketDisplayComponent implements OnInit{
  private http: HttpClient;
  isTicketsLoading: boolean = false;
  tickets: Ticket[] = [];

  constructor(http: HttpClient) {
    this.http = http;
  }

  async ngOnInit(): Promise<void> {
    this.loadTickets(true);
    setInterval(() => this.loadTickets(), 1000);
  }

  loadTickets(initial: boolean = false): void {
    if (initial) this.isTicketsLoading = true;
    this.http.get<Ticket[]>("http://localhost:8080/tickets").subscribe((tickets) => {
      this.tickets = tickets;
      this.isTicketsLoading = false;
      console.log(tickets);
    });
  }

}
