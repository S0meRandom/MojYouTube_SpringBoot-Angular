import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Header} from '../../components/header/header';
import {Router} from '@angular/router';
import {CommonModule, NgForOf, NgIf} from '@angular/common';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {Sidebar} from '../../components/sidebar/sidebar';

@Component({
  selector: 'app-liked-videos',
  imports: [
    Header,
    NgForOf,
    NgIf, CommonModule, Sidebar
  ],
  templateUrl: './liked-videos.html',
  styleUrl: './liked-videos.css',
})
export class LikedVideos implements OnInit{
  videos: any[] = [];
  userData: any;

  constructor(private route: Router,private cdr: ChangeDetectorRef,private sanitizer:DomSanitizer) {}

  ngOnInit(): void {
        this.fetchLikedVideos();
        this.fetchLoggedUserData();
    }

  async fetchLikedVideos(){
    try{
      const response = await fetch("http://localhost:8080/api/video/likedVideos",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.videos = await response.json();
        this.cdr.detectChanges();
      }

    }catch(error){

    }
  }
  async fetchLoggedUserData(){
    try{
      const response = await fetch("http://localhost:8080/api/users/me",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.userData = await response.json();
      }

    }catch(error){

    }
  }
  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }
  goLikedVideo(id:string){
    this.route.navigate(['videoDetailPage',id])
  }
}
