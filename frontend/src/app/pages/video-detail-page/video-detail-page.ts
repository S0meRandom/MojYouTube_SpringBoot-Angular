import {ChangeDetectorRef, Component, OnInit, OnDestroy, TemplateRef} from '@angular/core';
import { ActivatedRoute ,Router} from '@angular/router';
import {Header} from '../../components/header/header';
import {NgForOf} from '@angular/common';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatDialog, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';

@Component({
  selector: 'app-video-detail-page',
  imports: [
    Header,
    NgForOf,
    NgIf,
    FormsModule,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle
  ],
  templateUrl: './video-detail-page.html',
  styleUrl: './video-detail-page.css',
})
export class VideoDetailPage implements OnInit,OnDestroy{
  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef,
              private sanitizer: DomSanitizer,private dialog: MatDialog,private router: Router){}
  videoId: string | null = null;
  sidebarVideos:any [] = [];
  playlists: any [] = [];
  videoUrl: SafeUrl | null = null;
  videoData: any = null;
  videosChannelData: any = null;
  private viewTimeout: any;
  userReaction: string | null = null;
  dialogRef?: MatDialogRef<any>;
  selectedPlaylist: any = null;



  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');

      if (id) {
        this.videoId = id;
        this.setupVideoPage(id);
      }
    });

  }
  private setupVideoPage(id: string) {

    const rawUrl = `http://localhost:8080/api/video/videoPlay/${id}`;
    this.videoUrl = this.sanitizer.bypassSecurityTrustUrl(rawUrl);
    this.fetchVideoData(id);
    this.fetchSidebarVideos();
    this.handleViewUpdate();
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
  async fetchUserPlaylists(){
    try{
      const response = await fetch("http://localhost:8080/api/playlists/loggerUserPlaylists",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.cdr.detectChanges();
        this.playlists = await response.json();
      }else{
        alert("Błąd w trakcie pobierania playlist użytkownika");
      }

    }catch(error){

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
    if (this.viewTimeout) {
      clearTimeout(this.viewTimeout);
    }
    this.viewTimeout = setTimeout(()=>{
      this.updateView(this.videoId);
    },10000);
  }
  async subscribe(){

  }
  async handleReaction(id: any,type:string){
    const oldReaction = this.userReaction;
    if(this.userReaction === type){
      this.userReaction = null;
    }else{
      this.userReaction = type;
    }

    try{
    const response = await fetch(`http://localhost:8080/api/video/react/${id}?type=${type}`,{
      method: 'POST',
      credentials: 'include'
    });
    if(response.ok){
      await this.fetchVideoData(this.videoId);
    }
    else{
      this.userReaction = oldReaction;
    }
  }catch(error){
    throw new Error()
    }
  }
  async addVideoToPlaylist(videoId:number,playlistId:number){
    try{
      const response = await fetch(`http://localhost:8080/api/playlists/addToPlaylist/${videoId}/${playlistId}`,{
        method: 'PUT',
        credentials: 'include'
      });
      if(response.ok){
        this.closeDialog();
      }else{
        alert("Błąd w trakcie dodawania do playlisty");
      }

    }catch(error){

    }
  }
  openAddToPlaylistDialog(templateRef: TemplateRef<any>){

    this.fetchUserPlaylists().then(r => this.dialogRef = this.dialog.open(templateRef));
  }
  closeDialog(){
    this.dialogRef?.close();
  }
  goSidebarVideo(id: any){
    this.router.navigate(['/videoDetailPage',id]);
  }

}


