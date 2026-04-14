import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  constructor(private router:Router){}
  goPlayListPage(){
    this.router.navigate(['playlistPage']);

  }
  goLikedVideosPage(){
    this.router.navigate(['likedVideos']);
  }
  logout(){}
}
