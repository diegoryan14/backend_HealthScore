import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { AnuncioService } from '../service/anuncio.service';
import { IAnuncio } from '../anuncio.model';
import { AnuncioFormService } from './anuncio-form.service';

import { AnuncioUpdateComponent } from './anuncio-update.component';

describe('Anuncio Management Update Component', () => {
  let comp: AnuncioUpdateComponent;
  let fixture: ComponentFixture<AnuncioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let anuncioFormService: AnuncioFormService;
  let anuncioService: AnuncioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AnuncioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AnuncioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnuncioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    anuncioFormService = TestBed.inject(AnuncioFormService);
    anuncioService = TestBed.inject(AnuncioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const anuncio: IAnuncio = { id: 456 };

      activatedRoute.data = of({ anuncio });
      comp.ngOnInit();

      expect(comp.anuncio).toEqual(anuncio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnuncio>>();
      const anuncio = { id: 123 };
      jest.spyOn(anuncioFormService, 'getAnuncio').mockReturnValue(anuncio);
      jest.spyOn(anuncioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anuncio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anuncio }));
      saveSubject.complete();

      // THEN
      expect(anuncioFormService.getAnuncio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(anuncioService.update).toHaveBeenCalledWith(expect.objectContaining(anuncio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnuncio>>();
      const anuncio = { id: 123 };
      jest.spyOn(anuncioFormService, 'getAnuncio').mockReturnValue({ id: null });
      jest.spyOn(anuncioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anuncio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anuncio }));
      saveSubject.complete();

      // THEN
      expect(anuncioFormService.getAnuncio).toHaveBeenCalled();
      expect(anuncioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnuncio>>();
      const anuncio = { id: 123 };
      jest.spyOn(anuncioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anuncio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(anuncioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
