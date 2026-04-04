import {Router} from '@angular/router';
import {Header} from '../../components/header/header';
import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {NgForOf,NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MatDialog, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-playlist-page',
  imports: [
    Header, NgIf, NgForOf, FormsModule, MatDialogContent, MatDialogActions, MatDialogTitle, MatButton
  ],
  templateUrl: './playlist-page.html',
  styleUrl: './playlist-page.css',
})
export class PlaylistPage implements OnInit{
  playlists: any[] = [];
  dialogRef?: MatDialogRef<any>;
  playlistData = {
    playlistName: ''
  }

  constructor(private cdr: ChangeDetectorRef,private dialog: MatDialog,
              private router: Router){}

  openCreatePlaylistDialog(templateRef: TemplateRef<any>){
    this.dialogRef = this.dialog.open(templateRef);
  }
  closeDialog(){
    this.dialogRef?.close();
  }


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
        body: this.playlistData.playlistName
      });
      if(response.ok){
        await this.fetchUserPlaylists();
        this.closeDialog();
      }else{
        alert("Błąd w trakcie tworzenia playlisty");
      }

    }catch(error){

    }
  }
  async deletePlaylist(id:number){
    try{
      const response = await fetch(`http://localhost:8080/api/playlists/${id}`,{
        method: 'DELETE',
        credentials: 'include'
      });
      if(response.ok){
        await this.fetchUserPlaylists();
      }else{
        alert("Błąd w trakcie usuwania playlisty");
      }


    }catch(error){

    }
  }
  goPlaylistVideos(id:number){
    this.router.navigate(['playlistVideosPage',id]);

  }

}
