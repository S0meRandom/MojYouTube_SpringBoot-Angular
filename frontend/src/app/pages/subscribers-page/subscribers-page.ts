import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Header} from '../../components/header/header';
import {NgForOf, NgIf} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-subscribers-page',
  imports: [
    Header,
    NgForOf,
    NgIf
  ],
  templateUrl: './subscribers-page.html',
  styleUrl: './subscribers-page.css',
})
export class SubscribersPage implements OnInit{
  userData: any = null;

  constructor(private cdr: ChangeDetectorRef,private router: Router) {
  }
  userSubscriptions:any[] = [];
    async ngOnInit() {
      await this.getUserSubscriptions();
      await this.fetchUserData();
    }
    async getUserSubscriptions(){
      try{
        const response = await fetch("http://localhost:8080/api/users/userSubscriptions",{
          method:'GET',
          credentials: 'include'
        });
        if(response.ok){
          this.userSubscriptions = await response.json();
          this.cdr.detectChanges();
        }

      }catch(error){

      }
    }
  async fetchUserData(){
    try{
      const response = await fetch("http://localhost:8080/api/users/me",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.userData = await response.json();
        this.cdr.detectChanges();
      }

    }catch(error){

    }
  }
  goSubscriberChannel(id:string){
      this.router.navigate(['channelPage',id]);
  }

}
