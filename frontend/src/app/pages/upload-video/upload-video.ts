import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import { NgIf } from '@angular/common';
import {Header} from '../../components/header/header';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-upload-video',
  imports: [
    FormsModule,
    NgIf,
    Header
  ],
  templateUrl: './upload-video.html',
  styleUrl: './upload-video.css',
})
export class UploadVideo {
  constructor(private router: Router, private http: HttpClient,private cdr: ChangeDetectorRef){}

  selectedVideoFile: File | null = null;
  selectedThumbnailFile: File | null = null;
  uploadError = false;


  videoData = {
    title: '',
    description: ''
  }
  onVideoSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedVideoFile = file;
    }
  }
  onThumbnailSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedThumbnailFile = file;
    }
  }

  showErrorMessage(){
    this.uploadError = true;
  }

  async uploadVideo(){
    if (!this.selectedVideoFile || !this.selectedThumbnailFile) {
      alert('Proszę wybrać zarówno plik wideo, jak i miniaturkę!');
      return;
    }
    const formData = new FormData();
    formData.append('title',this.videoData.title);
    formData.append('description',this.videoData.description);
    formData.append('videoFile',this.selectedVideoFile);
    formData.append('thumbnailFile',this.selectedThumbnailFile);

    this.http.post('http://localhost:8080/api/video/upload', formData, {
      withCredentials: true
    }).subscribe({
      next: (res) => alert("Wgrano pomyślnie!"),
      error: (err) => {
        console.error(err);
        this.showErrorMessage();
        this.cdr.detectChanges();
      }
    });


  }

}
