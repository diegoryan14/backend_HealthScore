import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { QualidadeSonoDetailComponent } from './qualidade-sono-detail.component';

describe('QualidadeSono Management Detail Component', () => {
  let comp: QualidadeSonoDetailComponent;
  let fixture: ComponentFixture<QualidadeSonoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QualidadeSonoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QualidadeSonoDetailComponent,
              resolve: { qualidadeSono: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QualidadeSonoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QualidadeSonoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load qualidadeSono on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QualidadeSonoDetailComponent);

      // THEN
      expect(instance.qualidadeSono()).toEqual(expect.objectContaining({ id: 123 }));
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
