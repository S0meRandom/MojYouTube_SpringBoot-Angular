import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [
    FormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  constructor(private router: Router){}
  userData={
    username: '',
    email: '',
    password: '',
    channelName: ''
  };


  async Register(){
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
