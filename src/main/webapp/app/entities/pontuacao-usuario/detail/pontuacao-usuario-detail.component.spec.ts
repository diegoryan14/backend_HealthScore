import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PontuacaoUsuarioDetailComponent } from './pontuacao-usuario-detail.component';

describe('PontuacaoUsuario Management Detail Component', () => {
  let comp: PontuacaoUsuarioDetailComponent;
  let fixture: ComponentFixture<PontuacaoUsuarioDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PontuacaoUsuarioDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PontuacaoUsuarioDetailComponent,
              resolve: { pontuacaoUsuario: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PontuacaoUsuarioDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PontuacaoUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pontuacaoUsuario on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PontuacaoUsuarioDetailComponent);

      // THEN
      expect(instance.pontuacaoUsuario()).toEqual(expect.objectContaining({ id: 123 }));
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
