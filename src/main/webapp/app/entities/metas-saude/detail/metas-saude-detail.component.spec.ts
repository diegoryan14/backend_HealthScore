import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MetasSaudeDetailComponent } from './metas-saude-detail.component';

describe('MetasSaude Management Detail Component', () => {
  let comp: MetasSaudeDetailComponent;
  let fixture: ComponentFixture<MetasSaudeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetasSaudeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MetasSaudeDetailComponent,
              resolve: { metasSaude: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MetasSaudeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetasSaudeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load metasSaude on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MetasSaudeDetailComponent);

      // THEN
      expect(instance.metasSaude()).toEqual(expect.objectContaining({ id: 123 }));
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
