import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Company } from './company';

describe('Company', () => {
  let component: Company;
  let fixture: ComponentFixture<Company>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Company]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Company);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
