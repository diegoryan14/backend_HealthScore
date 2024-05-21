import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ControleMedicamentosDetailComponent } from './controle-medicamentos-detail.component';

describe('ControleMedicamentos Management Detail Component', () => {
  let comp: ControleMedicamentosDetailComponent;
  let fixture: ComponentFixture<ControleMedicamentosDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ControleMedicamentosDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ControleMedicamentosDetailComponent,
              resolve: { controleMedicamentos: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ControleMedicamentosDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControleMedicamentosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load controleMedicamentos on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ControleMedicamentosDetailComponent);

      // THEN
      expect(instance.controleMedicamentos()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
