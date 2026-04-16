import {Component, OnInit} from '@angular/core';
import {Header} from '../../components/header/header';
import {Sidebar} from '../../components/sidebar/sidebar';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-channel',
  imports: [
    Header,
    Sidebar,
    FormsModule
  ],
  templateUrl: './edit-channel.html',
  styleUrl: './edit-channel.css',
})
export class EditChannel implements  OnInit{
  constructor(private router:Router) {
  }
  ngOnInit(): void {

  }
  newChannelName:string = '';
  newChannelCountry:string = '';
  newChannelDescription:string = '';


  async changeChannelInfo(){
    const body = {
      newChannelName: this.newChannelName,
      newChannelCountry: this.newChannelCountry,
      newChannelDescription: this.newChannelDescription
    }
    try{
      const response = await fetch("http://localhost:8080/api/channel/editChannelInfo",{
        method:'PUT',
        credentials: 'include',
        body: JSON.stringify(body),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if(response.ok){
        this.goHomePage();
      }

    }catch(error){

    }
  }
  goHomePage(){
    this.router.navigate(['home']);
  }
}
