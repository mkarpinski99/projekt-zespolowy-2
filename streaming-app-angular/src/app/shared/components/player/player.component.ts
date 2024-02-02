import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import videojs from 'video.js';

@Component({
  selector: 'app-player',
  standalone: true,
  imports: [],
  templateUrl: './player.component.html',
  styleUrl: './player.component.scss'
})
export class PlayerComponent implements OnInit, OnDestroy {
  @ViewChild('target', { static: true })
  target!: ElementRef;

  // See options: https://videojs.com/guides/options
  @Input()
  options = {
    fluid: true,
    aspectRatio: "16:9",
    autoplay: false,
    sources: [{
      src: 'http://localhost:8080/live/test.m3u8',
      type: 'application/x-mpegURL'
    }],
    fill: true,
    liveui: true,
    poster: "https://robohash.org/user3",
    controls: true,
  }

  player: any;

  constructor(
    private elementRef: ElementRef,
  ) {}

  ngOnInit() {
    this.player = videojs(this.target.nativeElement, this.options, function onPlayerReady() {
      console.log('onPlayerReady');
    });
  }

  ngOnDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  }
}
