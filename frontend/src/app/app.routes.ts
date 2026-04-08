import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Home} from './pages/home/home';
import {Register} from './pages/register/register';
import {UploadVideo} from './pages/upload-video/upload-video';
import {ChannelPage} from './pages/channel-page/channel-page';
import {VideoDetailPage} from './pages/video-detail-page/video-detail-page';
import {PlaylistPage} from './pages/playlist-page/playlist-page';
import {PlaylistVideosPage} from './pages/playlist-videos-page/playlist-videos-page';
import {LikedVideos} from './pages/liked-videos/liked-videos';




export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'home', component: Home },
  {path: 'register', component: Register},
  {path: 'upload-video', component: UploadVideo},
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {path: 'channelPage/:id',component: ChannelPage},
  {path: 'videoDetailPage/:id',component: VideoDetailPage},
  {path: 'playlistPage', component: PlaylistPage},
  {path: 'playlistVideosPage/:id', component: PlaylistVideosPage},
  {path: 'likedVideos', component: LikedVideos}

];
