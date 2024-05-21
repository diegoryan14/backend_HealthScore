import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsumoAguaDetailComponent } from './consumo-agua-detail.component';

describe('ConsumoAgua Management Detail Component', () => {
  let comp: ConsumoAguaDetailComponent;
  let fixture: ComponentFixture<ConsumoAguaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsumoAguaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsumoAguaDetailComponent,
              resolve: { consumoAgua: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsumoAguaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsumoAguaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load consumoAgua on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsumoAguaDetailComponent);

      // THEN
      expect(instance.consumoAgua()).toEqual(expect.objectContaining({ id: 123 }));
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
