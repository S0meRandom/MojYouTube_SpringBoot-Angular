import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscribersPage } from './subscribers-page';

describe('SubscribersPage', () => {
  let component: SubscribersPage;
  let fixture: ComponentFixture<SubscribersPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscribersPage],
    }).compileComponents();

    fixture = TestBed.createComponent(SubscribersPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
