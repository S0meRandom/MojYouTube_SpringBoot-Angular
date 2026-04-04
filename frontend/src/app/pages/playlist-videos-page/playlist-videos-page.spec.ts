import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaylistVideosPage } from './playlist-videos-page';

describe('PlaylistVideosPage', () => {
  let component: PlaylistVideosPage;
  let fixture: ComponentFixture<PlaylistVideosPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaylistVideosPage],
    }).compileComponents();

    fixture = TestBed.createComponent(PlaylistVideosPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
