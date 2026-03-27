import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChannelPage } from './channel-page';

describe('ChannelPage', () => {
  let component: ChannelPage;
  let fixture: ComponentFixture<ChannelPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChannelPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ChannelPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
