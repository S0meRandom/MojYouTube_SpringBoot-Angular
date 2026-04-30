import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  constructor(private router: Router){}
  showPasswordError: boolean = false;
  showUsernameError: boolean = false;
  showEmailError: boolean = false;
  showChannelNameErorr: boolean = false;
  userData={
    username: '',
    email: '',
    password: '',
    channelName: ''
  };


  async Register(){
    if(this.userData.password.length < 8){
      this.showPasswordError = true;
    }
    else{
      const response = await fetch("http://localhost:8080/api/users/register",{
        method:'POST',
        body: JSON.stringify(this.userData),
        headers: {'Content-Type': 'application/json'},
        credentials: 'include'
      });
      if(response.ok){
        alert("Pomyślnie zarejstrowano konto!");
      }
    }


  }

}
