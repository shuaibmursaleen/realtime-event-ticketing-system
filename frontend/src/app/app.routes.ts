import { Routes } from '@angular/router';
import { ConfigurationFormComponent } from './configuration-form/configuration-form.component';
import { TicketDisplayComponent } from './ticket-display/ticket-display.component';
import { LogDisplayComponent } from './log-display/log-display.component';
import { VendorComponent } from './vendor/vendor.component';
import { CustomerComponent } from './customer/customer.component';
import { ControlPanelComponent } from './control-panel/control-panel.component';

export const routes: Routes = [
  { path: 'config', component: ConfigurationFormComponent },
  { path: 'ticket-display', component: TicketDisplayComponent },
  { path: 'log-display', component: LogDisplayComponent },
  { path: 'vendors', component: VendorComponent },
  { path: 'customers', component: CustomerComponent },
  { path: '', component: ControlPanelComponent },
];
