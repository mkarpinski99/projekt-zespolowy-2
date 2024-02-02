import { Component, Input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-channels-list',
  standalone: true,
  imports: [RouterModule, MatProgressSpinnerModule],
  templateUrl: './channels-list.component.html',
  styleUrl: './channels-list.component.scss'
})
export class ChannelsListComponent {
  @Input()
  channels: Array<any> = []

  @Input()
  loading: boolean = true;
}
