import {Component, ViewChild, ElementRef, AfterViewInit, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ChangeDetectorRef } from '@angular/core';
import {FormsModule} from '@angular/forms';
@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements AfterViewInit, OnInit{

  constructor(private router: Router, private sanitizer: DomSanitizer,private cdr: ChangeDetectorRef){}
  @ViewChild('videoContainer') container!: ElementRef;
  videos: any[] = [];
  userData: any = null;
  searchQuery: string = '';

  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }
  goVideoDetail(videoId: number){
    this.router.navigate(['/videoDetailPage',videoId]);
  }
  goPlayListPage(){
    this.router.navigate(['/playlistPage']);
  }
  goLikedVideosPage(){
    this.router.navigate(['/likedVideos']);
  }

  ngOnInit(): void {
        this.fetchVideos();
        this.fetchUserData();
    }


  ngAfterViewInit(): void {
    console.log('Szerokość kontenera:', this.container.nativeElement.offsetWidth);
    }
  goUploadVideo(){
    this.router.navigate(['/upload-video']);
  }
  goChannelPage(){
    this.router.navigate(['channelPage',this.userData.id]);
  }
  goSubscribersPage(){
    this.router.navigate(['subscribersPage']);
  }
  goLoginPage(){
    this.router.navigate(['login']);
  }

  async fetchVideos(){
    const response = await fetch("http://localhost:8080/api/video",{
      method : 'GET',
      credentials: 'include'
    });
    if(response.ok){
      this.videos = await response.json();
      this.cdr.detectChanges();
    }else{
      console.error("Problem z pobraniem filmów");
    }

  }
  async fetchUserData(){
    const response = await fetch("http://localhost:8080/api/users/me",{
      method: 'GET',
      credentials: 'include'
    });
    if(response.ok){
      this.userData = await response.json();
    }else{
      console.error("Błąd w trakcie ładowania danych użytkownika");
    }

  }
  async searchVideos(){
    if (!this.searchQuery || this.searchQuery.trim() === '') {
      await this.fetchVideos();
      return;
    }
    try{
      const response = await fetch(`http://localhost:8080/api/video/searchVideos?query=${this.searchQuery}`,{
        method: 'GET'
      });
      if(response.ok){
        this.videos = await response.json();
        this.cdr.detectChanges();
      }

    }catch(error){

    }
  }
  async logout(){
    try{
      const response = await fetch("http://localhost:8080/api/auth/logout",{
        method: 'POST',
        credentials: 'include'
      });
      if(response.ok){
        this.goLoginPage();
      }

    }catch(error){

    }
  }



}
