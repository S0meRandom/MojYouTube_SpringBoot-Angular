import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  constructor(private router: Router) {}


  goHomePage(){
    this.router.navigate(['home']);
  }
  goChannelPage(){
    this.router.navigate(['channelPage']);
  }




}


