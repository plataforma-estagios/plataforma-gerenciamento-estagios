import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormJob } from './form-job';

describe('FormJob', () => {
  let component: FormJob;
  let fixture: ComponentFixture<FormJob>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormJob]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormJob);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
