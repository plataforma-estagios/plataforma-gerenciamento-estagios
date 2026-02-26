import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteJob } from './delete-job';

describe('DeleteJob', () => {
  let component: DeleteJob;
  let fixture: ComponentFixture<DeleteJob>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteJob]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DeleteJob);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
