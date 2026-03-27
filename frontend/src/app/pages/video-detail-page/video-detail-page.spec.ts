import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoDetailPage } from './video-detail-page';

describe('VideoDetailPage', () => {
  let component: VideoDetailPage;
  let fixture: ComponentFixture<VideoDetailPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoDetailPage],
    }).compileComponents();

    fixture = TestBed.createComponent(VideoDetailPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
