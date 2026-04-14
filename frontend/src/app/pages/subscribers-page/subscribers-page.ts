import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Header} from '../../components/header/header';
import {NgForOf, NgIf} from '@angular/common';

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

  constructor(private cdr: ChangeDetectorRef) {
  }
  userSubscriptions:any[] = [];
    ngOnInit(): void {
      this.getUserSubscriptions();
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

}
