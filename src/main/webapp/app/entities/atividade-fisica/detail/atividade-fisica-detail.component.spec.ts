import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AtividadeFisicaDetailComponent } from './atividade-fisica-detail.component';

describe('AtividadeFisica Management Detail Component', () => {
  let comp: AtividadeFisicaDetailComponent;
  let fixture: ComponentFixture<AtividadeFisicaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AtividadeFisicaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AtividadeFisicaDetailComponent,
              resolve: { atividadeFisica: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AtividadeFisicaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AtividadeFisicaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load atividadeFisica on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AtividadeFisicaDetailComponent);

      // THEN
      expect(instance.atividadeFisica()).toEqual(expect.objectContaining({ id: 123 }));
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
