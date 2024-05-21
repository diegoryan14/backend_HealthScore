import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnuncioDetailComponent } from './anuncio-detail.component';

describe('Anuncio Management Detail Component', () => {
  let comp: AnuncioDetailComponent;
  let fixture: ComponentFixture<AnuncioDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnuncioDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AnuncioDetailComponent,
              resolve: { anuncio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnuncioDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnuncioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load anuncio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnuncioDetailComponent);

      // THEN
      expect(instance.anuncio()).toEqual(expect.objectContaining({ id: 123 }));
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
