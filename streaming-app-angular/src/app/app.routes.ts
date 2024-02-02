import { Routes } from '@angular/router';
import { ContentLayoutComponent } from './layout/content-layout/content-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: ContentLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
            import('@features/home/home.module').then(m => m.HomeModule)
      },
      {
        path: 'auth',
        loadChildren: () =>
            import('@features/auth/auth.module').then(m => m.AuthModule)
      },
      {
        path: 'channel',
        loadChildren: () =>
            import('@features/channel/channel.module').then(m => m.ChannelModule)
      },
      {
        path: 'category',
        loadChildren: () =>
            import('@features/category/category.module').then(m => m.CategoryModule)
      },
      {
        path: 'settings',
        loadChildren: () =>
            import('@features/user/user.module').then(m => m.UserModule)
      },
    ]
  }
];
