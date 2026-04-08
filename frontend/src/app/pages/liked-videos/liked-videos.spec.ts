import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LikedVideos } from './liked-videos';

describe('LikedVideos', () => {
  let component: LikedVideos;
  let fixture: ComponentFixture<LikedVideos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LikedVideos],
    }).compileComponents();

    fixture = TestBed.createComponent(LikedVideos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
