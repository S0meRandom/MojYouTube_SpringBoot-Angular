import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Header} from '../../components/header/header';
import {NgForOf} from '@angular/common';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-video-detail-page',
  imports: [
    Header,
    NgForOf,
    NgIf
  ],
  templateUrl: './video-detail-page.html',
  styleUrl: './video-detail-page.css',
})
export class VideoDetailPage implements OnInit{
  videoId: string | null = null;
  videos:any [] = [];
  videoUrl: SafeUrl | null = null;

  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef,
              private sanitizer: DomSanitizer){}

  ngOnInit(){
    this.videoId = this.route.snapshot.paramMap.get('id');
    const rawUrl = `http://localhost:8080/api/video/videoPlay/${this.videoId}`;
    this.videoUrl = this.sanitizer.bypassSecurityTrustUrl(rawUrl);
    this.fetchVideos();
  }
  async fetchVideos(){
    const response = await fetch("http://localhost:8080/api/video",{
      method: 'GET',
      credentials:'include'
    })
    if(response.ok){
      this.videos = await response.json();
      this.cdr.detectChanges();
    }
  }
  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }



}
