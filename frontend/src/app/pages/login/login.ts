import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, NgIf],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(private router: Router){}
  login = '';
  password='';
  loginError = false;

  goHome(){
    console.log("Przenoszę na główną");
    this.router.navigate(['/home']);
  }
  async Login(){
    const params = new URLSearchParams();
    params.append('username',this.login);
    params.append('password',this.password);

    const response = await fetch('http://localhost:8080/api/auth/login',{
      method:'POST',
      body: params,
      headers: {
        'Content-Type' : 'application/x-www-form-urlencoded'
      },
      credentials:'include'
    });
    if(response.ok){
      this.goHome();
    }else{
      if(this.login == ''){
        alert("Login jest pusty!");

      }
      else if(this.password==''){
        alert("Hasło jest puste!");
      }
      else if(response.status===401){
        this.showMessage();

      }
    }

  }

  showMessage(){
    this.loginError = true;
  }
}
