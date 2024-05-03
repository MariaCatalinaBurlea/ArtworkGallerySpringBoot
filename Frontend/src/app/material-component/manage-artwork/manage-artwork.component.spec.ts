import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageArtworkComponent } from './manage-artwork.component';

describe('ManageArtworkComponent', () => {
  let component: ManageArtworkComponent;
  let fixture: ComponentFixture<ManageArtworkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageArtworkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageArtworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
