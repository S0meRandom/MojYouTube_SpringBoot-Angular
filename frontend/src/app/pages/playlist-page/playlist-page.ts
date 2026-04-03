
import {Header} from '../../components/header/header';
import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgForOf,NgIf} from '@angular/common';

@Component({
  selector: 'app-playlist-page',
  imports: [
    Header,NgIf,NgForOf
  ],
  templateUrl: './playlist-page.html',
  styleUrl: './playlist-page.css',
})
export class PlaylistPage implements OnInit{
  playlists: any[] = [];
  playlistData = {
    playlistName: "test"
  }

  constructor(private cdr: ChangeDetectorRef){}


  ngOnInit(): void {
    this.fetchUserPlaylists();

  }

  async fetchUserPlaylists(){
    try{
      const response = await fetch("http://localhost:8080/api/playlists/loggerUserPlaylists",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.playlists = await response.json();
        this.cdr.detectChanges();
      }else{

      }

    }catch(error){

    }
  }
  async createPlaylist(){
    try{
      const response = await fetch("http://localhost:8080/api/playlists",{
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.playlistData)
      });
      if(response.ok){
        await this.fetchUserPlaylists();
      }else{
        alert("Błąd w trakcie tworzenia playlisty");
      }

    }catch(error){

    }
  }
}
