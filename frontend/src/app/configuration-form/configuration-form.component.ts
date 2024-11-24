import { HttpClient } from '@angular/common/http';
import { Component, Injectable, numberAttribute, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Configuration {
  total_tickets?: number;
  ticket_release_rate?: number;
  customer_retrieval_rate?: number;
  max_ticket_capacity?: number;
  release_interval?: number;
}

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './configuration-form.component.html',
  styles: []
})

export class ConfigurationFormComponent implements OnInit{
  http: HttpClient;
  config: Configuration | undefined = undefined;

  totalTickets: number | undefined = undefined;
  ticketReleaseRate: number | undefined = undefined;
  customerRetrievalRate: number | undefined = undefined;
  maxTicketCapacity: number | undefined = undefined;
  releaseInterval: number | undefined = undefined;

  constructor(http:HttpClient) {
    this.http = http;
  }

  async ngOnInit(): Promise<void> {
    this.getConfig();
  }

  getConfig(): void {
    this.http.get<Configuration> ("http://localhost:8080/configuration").subscribe((config) => {
      this.config = config;
      console.log(config);
    });
  }

  setConfig(total_tickets?: number, ticket_release_rate?: number, customer_retrieval_rate?: number, max_ticket_capacity?: number, release_interval?: number): void {
    console.log(total_tickets, ticket_release_rate, customer_retrieval_rate, max_ticket_capacity, release_interval);
    this.http.post("http://localhost:8080/configuration", {total_tickets, ticket_release_rate, customer_retrieval_rate, max_ticket_capacity, release_interval}).subscribe(() => {
      this.getConfig();
    })
  }
}
