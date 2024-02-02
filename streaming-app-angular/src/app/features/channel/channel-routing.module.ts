import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChannelsComponent } from './view/channels/channels.component';
import { canActivateGuard } from 'app/core/auth/guards/can-activate.guard';
import { ChannelDetailsComponent } from './view/channel-details/channel-details.component';

const routes: Routes = [
  {
    path: 'list',
    component: ChannelsComponent
  },
  {
    path: 'category/:category',
    component: ChannelsComponent,
    pathMatch: 'full'
  },
  {
    path: 'followed',
    component: ChannelsComponent,
    canActivate: [canActivateGuard]
  },
  {
    path: ':username',
    component: ChannelDetailsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChannelRoutingModule { }
