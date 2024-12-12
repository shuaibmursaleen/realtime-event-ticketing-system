import { NgFor, NgIf } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './log-display.component.html',
  styles: [],
})
export class LogDisplayComponent implements OnInit {
  @ViewChild('display')
  latestLog!: ElementRef;
  logs: String[];
  noLogs: boolean;

  constructor(private host: AppComponent) {
    this.logs = [];
    this.noLogs = false;
  }

  async ngOnInit(): Promise<void> {
    this.loadLogs(true);
  }

  ngAfterViewChecked() {
    this.autoScroll();
  }

  async loadLogs(initial: boolean): Promise<void> {
    if (initial) this.noLogs = true;
    this.host.client
      .get('/logs', {
        responseType: 'stream',
        adapter: 'fetch',
      })
      .then(async (response) => {
        const stream = response.data;
        this.noLogs = false;

        const reader = stream.pipeThrough(new TextDecoderStream()).getReader();
        while (true) {
          const { value, done } = await reader.read();
          if (value == 'data:' || value == '' || value == ' ') {
            continue;
          } else {
            this.logs.push(value.replace('data:', ''));
          }
        }
      });
  }

  autoScroll(): void {
    this.latestLog.nativeElement.scrollTop =
      this.latestLog.nativeElement.scrollHeight;
  }
}
