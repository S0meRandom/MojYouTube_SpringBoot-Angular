import {ChangeDetectorRef, Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Header} from '../../components/header/header';
import {NgForOf, NgIf} from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-playlist-videos-page',
  imports: [
    Header,
    NgForOf,
    NgIf
  ],
  templateUrl: './playlist-videos-page.html',
  styleUrl: './playlist-videos-page.css',
})
export class PlaylistVideosPage implements OnInit,OnDestroy{
  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef,private sanitizer: DomSanitizer) {
  }

  ngOnDestroy(): void {
    if(this.viewTimeout){
      clearTimeout(this.viewTimeout);
    }

    }
  playlistId: string | null = null;
  videos: any [] = [];
  playlistData: any =null;
  private viewTimeout: any;

  ngOnInit(): void {
    this.playlistId = this.route.snapshot.paramMap.get('id');
    this.fetchPlaylistVideos(this.playlistId);
    this.fetchPlaylistData(this.playlistId);
    this.handleViewUpdate(this.videos[0].id);
    }
  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }
  getSafeVideoUrl(id:number): SafeUrl{
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/videoPlay/${id}`)
  }



    async fetchPlaylistVideos(playlistId:string | null){
    try{
      const response = await fetch(`http://localhost:8080/api/playlists/${playlistId}`,{
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
    async fetchPlaylistData(playlistId: string| null){
    try{
      const response = await fetch(`http://localhost:8080/api/playlists/playlistData/${playlistId}`,{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.playlistData = response.json();
      }else{
        alert("Błąd w trakcie pobierania danych o playliscie")
      }

    }catch(error){

    }
    }
  async updateView(id: string | null){
    const response = await fetch(`http://localhost:8080/api/video/${id}`,{
      method: 'PUT'
    });
    if(response.ok){
      this.cdr.detectChanges();
    }

  }
  handleViewUpdate(id: string | null){
    if (this.viewTimeout) {
      clearTimeout(this.viewTimeout);
    }
    this.viewTimeout = setTimeout(()=>{
      this.updateView(id);
    },10000);
  }

}
