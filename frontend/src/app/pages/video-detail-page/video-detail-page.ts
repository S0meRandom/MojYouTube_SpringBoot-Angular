import {ChangeDetectorRef, Component, OnInit,OnDestroy} from '@angular/core';
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
export class VideoDetailPage implements OnInit,OnDestroy{
  videoId: string | null = null;
  sidebarVideos:any [] = [];
  videoUrl: SafeUrl | null = null;
  videoData: any = null;
  videosChannelData: any = null;
  private viewTimeout: any;
  userReaction: string | null = null;

  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef,
              private sanitizer: DomSanitizer){}

  ngOnInit(){
    this.videoId = this.route.snapshot.paramMap.get('id');
    const rawUrl = `http://localhost:8080/api/video/videoPlay/${this.videoId}`;
    this.videoUrl = this.sanitizer.bypassSecurityTrustUrl(rawUrl);
    this.fetchSidebarVideos();
    this.fetchVideoData(this.videoId);
    this.fetchVideosChannelData(this.videoData.channel.id);
    this.handleViewUpdate();
  }
  ngOnDestroy(){
    if(this.viewTimeout){
      clearTimeout(this.viewTimeout);
    }
  }
  async fetchVideoData(id: String | null){
    const response = await fetch(`http://localhost:8080/api/video/${id}`,{
      method: 'GET'
    })
    if(response.ok){
      this.videoData = await response.json();

    }
  }
  async fetchVideosChannelData(id: any){
    const response = await fetch(`http://localhost:8080/api/channel/${id}`,{
      method: 'GET'
    });
    if(response.ok){
      this.videosChannelData = await response.json();
    }
  }
  async fetchSidebarVideos(){
    const response = await fetch("http://localhost:8080/api/video",{
      method: 'GET',
      credentials:'include'
    })
    if(response.ok){
      this.sidebarVideos = await response.json();
      this.cdr.detectChanges();
    }
  }
  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }

  async updateView(id: string | null){
      const response = await fetch(`http://localhost:8080/api/video/${id}`,{
        method: 'PUT'
      });
      if(response.ok){
        this.cdr.detectChanges();
      }

  }
  handleViewUpdate(){
    this.viewTimeout = setTimeout(()=>{
      this.updateView(this.videoId);
    },10000);
  }
  async subscribe(){

  }
  async handleReaction(id: any,type:String){
    const response = await fetch(`http://localhost:8080/api/video/${id}/react?type=${type}`,{
      method: 'POST',
      credentials: 'include'
    });
    if(response.ok){
      this.fetchVideoData(this.videoId);
    }
  }



}
