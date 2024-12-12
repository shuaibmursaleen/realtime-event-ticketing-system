import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [NgIf],
  templateUrl: './control-panel.component.html',
  styles: [],
})
export class ControlPanelComponent implements OnInit {
  status!: boolean;

  constructor(private host: AppComponent) {}

  async ngOnInit(): Promise<void> {
    this.getStatus();
  }

  async getStatus(): Promise<void> {
    try {
      const response = await this.host.client.get('/configuration');
      this.status = response.data.status;
    } catch (error) {
      this.status = false;
    }
  }

  async setStatus(status: boolean): Promise<void> {
    console.log(status);
    this.host.client.post('/configuration', {
      status: status,
    });
    this.getStatus();
  }
}
