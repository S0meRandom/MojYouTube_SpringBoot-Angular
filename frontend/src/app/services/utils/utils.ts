import { Component } from '@angular/core';
import { Injectable } from '@angular/core';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-utils',
  imports: [],
  templateUrl: './utils.html',
  styleUrl: './utils.css',
})
@Injectable({
  providedIn: 'root'
})
export class Utils {
  constructor(private sanitizer: DomSanitizer){}

  getSafeThumbnailUrl(id: number): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/thumbnail/${id}`);
  }
}
