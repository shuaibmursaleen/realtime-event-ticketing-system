import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from "@angular/router";

import axios, { Axios, AxiosInstance } from 'axios';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styles: [],
})

export class AppComponent {
  private instance: AxiosInstance;

  constructor() {
    this.instance = axios.create({
      baseURL: 'http://localhost:8080',
    });
  }

  get client() {
    return this.instance;
  }
}
