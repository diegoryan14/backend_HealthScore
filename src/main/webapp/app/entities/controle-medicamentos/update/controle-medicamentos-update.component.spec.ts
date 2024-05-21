import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ControleMedicamentosService } from '../service/controle-medicamentos.service';
import { IControleMedicamentos } from '../controle-medicamentos.model';
import { ControleMedicamentosFormService } from './controle-medicamentos-form.service';

import { ControleMedicamentosUpdateComponent } from './controle-medicamentos-update.component';

describe('ControleMedicamentos Management Update Component', () => {
  let comp: ControleMedicamentosUpdateComponent;
  let fixture: ComponentFixture<ControleMedicamentosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let controleMedicamentosFormService: ControleMedicamentosFormService;
  let controleMedicamentosService: ControleMedicamentosService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ControleMedicamentosUpdateComponent],
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
      .overrideTemplate(ControleMedicamentosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ControleMedicamentosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    controleMedicamentosFormService = TestBed.inject(ControleMedicamentosFormService);
    controleMedicamentosService = TestBed.inject(ControleMedicamentosService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const controleMedicamentos: IControleMedicamentos = { id: 456 };
      const internalUser: IUser = { id: 13260 };
      controleMedicamentos.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 18649 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ controleMedicamentos });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const controleMedicamentos: IControleMedicamentos = { id: 456 };
      const internalUser: IUser = { id: 23693 };
      controleMedicamentos.internalUser = internalUser;

      activatedRoute.data = of({ controleMedicamentos });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.controleMedicamentos).toEqual(controleMedicamentos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControleMedicamentos>>();
      const controleMedicamentos = { id: 123 };
      jest.spyOn(controleMedicamentosFormService, 'getControleMedicamentos').mockReturnValue(controleMedicamentos);
      jest.spyOn(controleMedicamentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controleMedicamentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: controleMedicamentos }));
      saveSubject.complete();

      // THEN
      expect(controleMedicamentosFormService.getControleMedicamentos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(controleMedicamentosService.update).toHaveBeenCalledWith(expect.objectContaining(controleMedicamentos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControleMedicamentos>>();
      const controleMedicamentos = { id: 123 };
      jest.spyOn(controleMedicamentosFormService, 'getControleMedicamentos').mockReturnValue({ id: null });
      jest.spyOn(controleMedicamentosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controleMedicamentos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: controleMedicamentos }));
      saveSubject.complete();

      // THEN
      expect(controleMedicamentosFormService.getControleMedicamentos).toHaveBeenCalled();
      expect(controleMedicamentosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControleMedicamentos>>();
      const controleMedicamentos = { id: 123 };
      jest.spyOn(controleMedicamentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controleMedicamentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(controleMedicamentosService.update).toHaveBeenCalled();
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
  });
});
