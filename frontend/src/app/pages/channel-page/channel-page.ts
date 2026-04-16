import { Component , OnInit} from '@angular/core';
import {Header} from '../../components/header/header';
import {ActivatedRoute, Router} from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import {Sidebar} from '../../components/sidebar/sidebar';

@Component({
  selector: 'app-channel-page',
  imports: [
    Header, NgIf, NgForOf, NgClass, Sidebar
  ],
  templateUrl: './channel-page.html',
  styleUrl: './channel-page.css',
})
export class ChannelPage implements OnInit{
  userId: string | null = null;
  channelData: any = null;
  channelVideos: any[] = [];
  isSubscribed:boolean = false;
  isMyChannel: boolean = false;
  constructor(private route: ActivatedRoute,private cdr: ChangeDetectorRef,
              private sanitizer: DomSanitizer,private router: Router) {}

  async ngOnInit(){
      this.userId = this.route.snapshot.paramMap.get('id');
      await this.getChannelData(this.userId);
      if(this.channelData){
        this.checkSubscribtion(this.channelData.id);
        this.checkIfChannelOwner();
      }
  }

  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }

  async getChannelData(id: string | null){
    const response = await fetch(`http://localhost:8080/api/channel/${id}`,{
      method: 'GET'
    });
    if(response.ok){
      this.channelData = await response.json();
      this.getChannelVideos(this.channelData.id);
      this.cdr.detectChanges();
    }else{
      alert("Problem z pobraniem danych kanału");
    }

  }

  async checkSubscribtion(channelId: any){
    try{
      const response = await fetch(`http://localhost:8080/api/users/me/checkSubscribtion/${channelId}`,{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.isSubscribed = true;
      }else{
        this.isSubscribed = false;
      }
      this.cdr.detectChanges();

    }catch(error){
      console.error("Błąd sieciowy");
      this.isSubscribed = false;
      this.cdr.detectChanges();
    }
  }


  async subscribeOrUnsubscribe(){
    try{
      const response = await fetch(`http://localhost:8080/api/channel/${this.channelData.id}`,{
        method: 'PUT'
      });
      if(response.ok){
        await this.getChannelData(this.userId);
      }

    }catch(error){
      alert("Błąd w trakcie próby subskrybowania");
    }

  }
  async getChannelVideos(id: any){
    try{
      const response = await fetch(`http://localhost:8080/api/channel/channelVideos/${id}`,{
        method: 'GET'
      });
      if(response.ok){
        this.channelVideos = await response.json();
        this.cdr.detectChanges();
      }

    }catch(error){
      alert("Błąd w trakcie ładowania filmików")
    }

  }
  async checkIfChannelOwner() {
    try {

      const response = await fetch(`http://localhost:8080/api/users/me`, {
        method: 'GET',
        credentials: 'include'
      });

      if (response.ok) {
        const currentUser = await response.json();

        this.isMyChannel = currentUser.id === this.channelData.id;
        this.cdr.detectChanges();
      }
    } catch (error) {
      console.error("Błąd"  );
      this.isMyChannel = false;
    }
  }
  goVideoDetail(videoId: number){
    this.router.navigate(['/videoDetailPage',videoId]);
  }
  goEditChannel(){
    this.router.navigate(['editChannelPage']);
  }

}
