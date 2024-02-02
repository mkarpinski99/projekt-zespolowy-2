import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, UrlSegment } from '@angular/router';
import { ChannelsListComponent } from '@features/channel/components/channels-list/channels-list.component';
import { ChannelService } from 'app/data/service/channel.service';

@Component({
  selector: 'app-channels',
  standalone: true,
  imports: [ChannelsListComponent],
  templateUrl: './channels.component.html',
  styleUrl: './channels.component.scss'
})
export class ChannelsComponent implements OnInit {
  pageTitle: string = "";
  channels: Array<any> = [];
  categoryName: string = "";
  loadingContents: boolean = true;

  @Input()
  set category(category: string) {
    if(category)
      this.pageTitle = category;
    this.categoryName = category;
  }

  constructor (
    private route: ActivatedRoute,
    private _channelService: ChannelService
  ) {}

  ngOnInit(): void {
    const path: UrlSegment[] = this.getRouteInfo();
    if(path.slice(-1)[0].path == "followed") {
      this.fetchFollowedChannels()
      this.pageTitle = "Obserwowane kanały";
    }
    else if(path.slice(-1)[0].path == "list") {
      this.fetchAllChannels();
      this.pageTitle = "Kanały";
    }
    else if(path.slice(-2)[0].path == 'category') {
      if(this.categoryName){
        this.fetchChannelsByCategory();
      }
    }
  }

  getRouteInfo(): UrlSegment[] {
    return this.route.snapshot.url;
  }

  fetchAllChannels(): void {
    this.loadingContents = true;
    this._channelService.getAllChannels().subscribe({
      next: result => this.channels = result,
      error: console.error,
      complete: () => this.loadingContents = false
    });
  }

  fetchChannelsByCategory(): void {
    this.loadingContents = true;
    this._channelService.getChannelsByCategory(this.categoryName).subscribe({
      next: result => this.channels = result,
      error: console.error,
      complete: () => this.loadingContents = false
    });
  }

  fetchFollowedChannels(): void {
    this.loadingContents = true;
    this._channelService.getFollowedChannels().subscribe({
      next: result => this.channels = result,
      error: console.error,
      complete: () => this.loadingContents = false
    });
  }
}
