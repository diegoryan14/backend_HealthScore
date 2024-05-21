import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ConsumoAguaService } from '../service/consumo-agua.service';
import { IConsumoAgua } from '../consumo-agua.model';
import { ConsumoAguaFormService } from './consumo-agua-form.service';

import { ConsumoAguaUpdateComponent } from './consumo-agua-update.component';

describe('ConsumoAgua Management Update Component', () => {
  let comp: ConsumoAguaUpdateComponent;
  let fixture: ComponentFixture<ConsumoAguaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consumoAguaFormService: ConsumoAguaFormService;
  let consumoAguaService: ConsumoAguaService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConsumoAguaUpdateComponent],
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
      .overrideTemplate(ConsumoAguaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsumoAguaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consumoAguaFormService = TestBed.inject(ConsumoAguaFormService);
    consumoAguaService = TestBed.inject(ConsumoAguaService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const consumoAgua: IConsumoAgua = { id: 456 };
      const internalUser: IUser = { id: 18151 };
      consumoAgua.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 15022 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consumoAgua });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consumoAgua: IConsumoAgua = { id: 456 };
      const internalUser: IUser = { id: 21437 };
      consumoAgua.internalUser = internalUser;

      activatedRoute.data = of({ consumoAgua });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.consumoAgua).toEqual(consumoAgua);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumoAgua>>();
      const consumoAgua = { id: 123 };
      jest.spyOn(consumoAguaFormService, 'getConsumoAgua').mockReturnValue(consumoAgua);
      jest.spyOn(consumoAguaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumoAgua });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumoAgua }));
      saveSubject.complete();

      // THEN
      expect(consumoAguaFormService.getConsumoAgua).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consumoAguaService.update).toHaveBeenCalledWith(expect.objectContaining(consumoAgua));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumoAgua>>();
      const consumoAgua = { id: 123 };
      jest.spyOn(consumoAguaFormService, 'getConsumoAgua').mockReturnValue({ id: null });
      jest.spyOn(consumoAguaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumoAgua: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumoAgua }));
      saveSubject.complete();

      // THEN
      expect(consumoAguaFormService.getConsumoAgua).toHaveBeenCalled();
      expect(consumoAguaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumoAgua>>();
      const consumoAgua = { id: 123 };
      jest.spyOn(consumoAguaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumoAgua });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consumoAguaService.update).toHaveBeenCalled();
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
