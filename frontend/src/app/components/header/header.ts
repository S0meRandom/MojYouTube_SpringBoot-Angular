import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit{
  userHeaderData: any = null;
  constructor(private router: Router) {}

  ngOnInit(): void {
        this.fetchHeaderUserData();
    }
  async fetchHeaderUserData(){
    try{
      const response = await fetch("http://localhost:8080/api/users/me",{
        method: 'GET',
        credentials: 'include'
      });
      if(response.ok){
        this.userHeaderData = await response.json();
      }

    }catch(error){

    }
  }


  goHomePage(){
    this.router.navigate(['home']);
  }
  goChannelPage(){
    this.router.navigate(['channelPage',this.userHeaderData.id]);
  }




}


