import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlaylistPage } from './playlist-page';
import { ChangeDetectorRef } from '@angular/core';
import {jest} from '@jest/globals'

describe('PlaylistPage', () => {
  let component: PlaylistPage;
  let fixture: ComponentFixture<PlaylistPage>;
  let cdr: ChangeDetectorRef;

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [PlaylistPage],
      providers: [

        { provide: ChangeDetectorRef, useValue: { detectChanges: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PlaylistPage);
    component = fixture.componentInstance;
    cdr = fixture.debugElement.injector.get(ChangeDetectorRef);


    jest.restoreAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

