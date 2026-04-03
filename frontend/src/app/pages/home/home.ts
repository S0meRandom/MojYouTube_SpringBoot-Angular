import {Component, ViewChild, ElementRef, AfterViewInit, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements AfterViewInit, OnInit{

  constructor(private router: Router, private sanitizer: DomSanitizer,private cdr: ChangeDetectorRef){}
  @ViewChild('videoContainer') container!: ElementRef;
  videos: any[] = [];
  userData: any = null;

  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }
  goVideoDetail(videoId: number){
    this.router.navigate(['/videoDetailPage',videoId]);
  }
  goPlayListPage(){
    this.router.navigate(['/playlistPage']);
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


}
