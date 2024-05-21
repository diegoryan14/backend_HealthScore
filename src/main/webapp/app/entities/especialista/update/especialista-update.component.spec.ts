import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { EspecialistaService } from '../service/especialista.service';
import { IEspecialista } from '../especialista.model';
import { EspecialistaFormService } from './especialista-form.service';

import { EspecialistaUpdateComponent } from './especialista-update.component';

describe('Especialista Management Update Component', () => {
  let comp: EspecialistaUpdateComponent;
  let fixture: ComponentFixture<EspecialistaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let especialistaFormService: EspecialistaFormService;
  let especialistaService: EspecialistaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EspecialistaUpdateComponent],
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
      .overrideTemplate(EspecialistaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspecialistaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    especialistaFormService = TestBed.inject(EspecialistaFormService);
    especialistaService = TestBed.inject(EspecialistaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const especialista: IEspecialista = { id: 456 };

      activatedRoute.data = of({ especialista });
      comp.ngOnInit();

      expect(comp.especialista).toEqual(especialista);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialista>>();
      const especialista = { id: 123 };
      jest.spyOn(especialistaFormService, 'getEspecialista').mockReturnValue(especialista);
      jest.spyOn(especialistaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialista }));
      saveSubject.complete();

      // THEN
      expect(especialistaFormService.getEspecialista).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(especialistaService.update).toHaveBeenCalledWith(expect.objectContaining(especialista));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialista>>();
      const especialista = { id: 123 };
      jest.spyOn(especialistaFormService, 'getEspecialista').mockReturnValue({ id: null });
      jest.spyOn(especialistaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialista: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialista }));
      saveSubject.complete();

      // THEN
      expect(especialistaFormService.getEspecialista).toHaveBeenCalled();
      expect(especialistaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialista>>();
      const especialista = { id: 123 };
      jest.spyOn(especialistaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(especialistaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
