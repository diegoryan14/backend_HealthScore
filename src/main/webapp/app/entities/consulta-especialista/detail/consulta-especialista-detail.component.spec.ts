import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsultaEspecialistaDetailComponent } from './consulta-especialista-detail.component';

describe('ConsultaEspecialista Management Detail Component', () => {
  let comp: ConsultaEspecialistaDetailComponent;
  let fixture: ComponentFixture<ConsultaEspecialistaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultaEspecialistaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsultaEspecialistaDetailComponent,
              resolve: { consultaEspecialista: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsultaEspecialistaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsultaEspecialistaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load consultaEspecialista on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsultaEspecialistaDetailComponent);

      // THEN
      expect(instance.consultaEspecialista()).toEqual(expect.objectContaining({ id: 123 }));
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
