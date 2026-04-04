import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Header} from '../../components/header/header';
import {NgForOf, NgIf} from '@angular/common';

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
export class PlaylistVideosPage implements OnInit{
  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef) {
  }
  playlistId: string | null = null;
  videos: any [] = [];
  playlistData: any =null;

  ngOnInit(): void {
    this.playlistId = this.route.snapshot.paramMap.get('id');
    this.fetchPlaylistVideos(this.playlistId);
    this.fetchPlaylistData(this.playlistId);
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
}
