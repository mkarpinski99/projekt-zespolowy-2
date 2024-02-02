import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ChannelsComponent } from '@features/channel/view/channels/channels.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ChannelsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  constructor(
    private _router: Router
  ) {
    _router.navigate(['/channel/list']);
  }

}
