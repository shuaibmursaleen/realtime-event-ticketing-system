import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { NgFor, NgIf } from '@angular/common';

interface Vendor {
  vendorId: number;
  ticketsPerRelease: number;
  releaseInterval: number;
  runStatus: boolean;
}

@Component({
  selector: 'app-vendor',
  standalone: true,
  imports: [FormsModule, NgIf, NgFor],
  templateUrl: './vendor.component.html',
  styles: [],
})
export class VendorComponent implements OnInit {
  vendors: Vendor[];
  ticketsPerRelease!: number;
  releaseInterval!: number;
  status!: boolean;

  constructor(private host: AppComponent) {
    this.vendors = [];
  }

  async ngOnInit(): Promise<void> {
    setInterval(() => this.getVendors(), 1000);
  }

  async getVendors(): Promise<void> {
    try {
      const response = await this.host.client.get('/vendors');
      this.vendors = response.data;
    } catch (error) {
      this.vendors = [];
    }
  }

  async setVendor(
    tickets_per_release: number,
    release_interval: number
  ): Promise<void> {
    this.host.client.post('/vendors', {
      tickets_per_release: tickets_per_release,
      release_interval: release_interval,
    });
    this.getVendors();
  }

  async toggleVendor(vendorId: number, status: boolean): Promise<void> {
    this.host.client.patch(`/vendors/${vendorId}`, {
      status: status,
    });
    this.getVendors();
  }

  async removeVendor(vendorId: number): Promise<void> {
    this.host.client.delete(`/vendors/${vendorId}`);
    this.getVendors();
  }
}
