import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IEspecialista } from 'app/entities/especialista/especialista.model';
import { EspecialistaService } from 'app/entities/especialista/service/especialista.service';
import { IConsultaEspecialista } from '../consulta-especialista.model';
import { ConsultaEspecialistaService } from '../service/consulta-especialista.service';
import { ConsultaEspecialistaFormService } from './consulta-especialista-form.service';

import { ConsultaEspecialistaUpdateComponent } from './consulta-especialista-update.component';

describe('ConsultaEspecialista Management Update Component', () => {
  let comp: ConsultaEspecialistaUpdateComponent;
  let fixture: ComponentFixture<ConsultaEspecialistaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consultaEspecialistaFormService: ConsultaEspecialistaFormService;
  let consultaEspecialistaService: ConsultaEspecialistaService;
  let userService: UserService;
  let especialistaService: EspecialistaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConsultaEspecialistaUpdateComponent],
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
      .overrideTemplate(ConsultaEspecialistaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsultaEspecialistaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consultaEspecialistaFormService = TestBed.inject(ConsultaEspecialistaFormService);
    consultaEspecialistaService = TestBed.inject(ConsultaEspecialistaService);
    userService = TestBed.inject(UserService);
    especialistaService = TestBed.inject(EspecialistaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const consultaEspecialista: IConsultaEspecialista = { id: 456 };
      const internalUser: IUser = { id: 31049 };
      consultaEspecialista.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 16935 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consultaEspecialista });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Especialista query and add missing value', () => {
      const consultaEspecialista: IConsultaEspecialista = { id: 456 };
      const especialista: IEspecialista = { id: 30944 };
      consultaEspecialista.especialista = especialista;

      const especialistaCollection: IEspecialista[] = [{ id: 14270 }];
      jest.spyOn(especialistaService, 'query').mockReturnValue(of(new HttpResponse({ body: especialistaCollection })));
      const additionalEspecialistas = [especialista];
      const expectedCollection: IEspecialista[] = [...additionalEspecialistas, ...especialistaCollection];
      jest.spyOn(especialistaService, 'addEspecialistaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consultaEspecialista });
      comp.ngOnInit();

      expect(especialistaService.query).toHaveBeenCalled();
      expect(especialistaService.addEspecialistaToCollectionIfMissing).toHaveBeenCalledWith(
        especialistaCollection,
        ...additionalEspecialistas.map(expect.objectContaining),
      );
      expect(comp.especialistasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consultaEspecialista: IConsultaEspecialista = { id: 456 };
      const internalUser: IUser = { id: 2919 };
      consultaEspecialista.internalUser = internalUser;
      const especialista: IEspecialista = { id: 20928 };
      consultaEspecialista.especialista = especialista;

      activatedRoute.data = of({ consultaEspecialista });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.especialistasSharedCollection).toContain(especialista);
      expect(comp.consultaEspecialista).toEqual(consultaEspecialista);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultaEspecialista>>();
      const consultaEspecialista = { id: 123 };
      jest.spyOn(consultaEspecialistaFormService, 'getConsultaEspecialista').mockReturnValue(consultaEspecialista);
      jest.spyOn(consultaEspecialistaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultaEspecialista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultaEspecialista }));
      saveSubject.complete();

      // THEN
      expect(consultaEspecialistaFormService.getConsultaEspecialista).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consultaEspecialistaService.update).toHaveBeenCalledWith(expect.objectContaining(consultaEspecialista));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultaEspecialista>>();
      const consultaEspecialista = { id: 123 };
      jest.spyOn(consultaEspecialistaFormService, 'getConsultaEspecialista').mockReturnValue({ id: null });
      jest.spyOn(consultaEspecialistaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultaEspecialista: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultaEspecialista }));
      saveSubject.complete();

      // THEN
      expect(consultaEspecialistaFormService.getConsultaEspecialista).toHaveBeenCalled();
      expect(consultaEspecialistaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultaEspecialista>>();
      const consultaEspecialista = { id: 123 };
      jest.spyOn(consultaEspecialistaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultaEspecialista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consultaEspecialistaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEspecialista', () => {
      it('Should forward to especialistaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(especialistaService, 'compareEspecialista');
        comp.compareEspecialista(entity, entity2);
        expect(especialistaService.compareEspecialista).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
