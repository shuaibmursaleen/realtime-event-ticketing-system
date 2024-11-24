import { NgIf, NgFor} from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConfigurationFormComponent } from "./configuration-form/configuration-form.component";
import { RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";

interface Vendor {
  vendorId: number;
  ticketsPerRelease: number;
  releaseInterval: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styles: [],
})

export class AppComponent {
}
