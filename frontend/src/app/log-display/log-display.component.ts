import { NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [NgIf],
  templateUrl: './log-display.component.html',
  styles: []
})
export class LogDisplayComponent implements OnInit{
  logs: String[] = [];
  noLogs:boolean = false;
  http: HttpClient;

  constructor(http: HttpClient) {
    this.http = http;
  }

  async ngOnInit(): Promise<void> {
    this.loadLogs(true);
    setInterval(() => this.loadLogs(false), 1000);
  }

  loadLogs(initial:boolean): void {
    if (initial) this.noLogs = true;
    this.http.get<String[]>("http://localhost:8080/logs").subscribe((logs) => {
      this.logs = logs;
      this.noLogs = false;
    })
  }
}
