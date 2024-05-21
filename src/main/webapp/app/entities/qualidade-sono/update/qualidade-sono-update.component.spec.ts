import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { QualidadeSonoService } from '../service/qualidade-sono.service';
import { IQualidadeSono } from '../qualidade-sono.model';
import { QualidadeSonoFormService } from './qualidade-sono-form.service';

import { QualidadeSonoUpdateComponent } from './qualidade-sono-update.component';

describe('QualidadeSono Management Update Component', () => {
  let comp: QualidadeSonoUpdateComponent;
  let fixture: ComponentFixture<QualidadeSonoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let qualidadeSonoFormService: QualidadeSonoFormService;
  let qualidadeSonoService: QualidadeSonoService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, QualidadeSonoUpdateComponent],
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
      .overrideTemplate(QualidadeSonoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QualidadeSonoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    qualidadeSonoFormService = TestBed.inject(QualidadeSonoFormService);
    qualidadeSonoService = TestBed.inject(QualidadeSonoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const qualidadeSono: IQualidadeSono = { id: 456 };
      const internalUser: IUser = { id: 12436 };
      qualidadeSono.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 9184 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ qualidadeSono });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const qualidadeSono: IQualidadeSono = { id: 456 };
      const internalUser: IUser = { id: 28044 };
      qualidadeSono.internalUser = internalUser;

      activatedRoute.data = of({ qualidadeSono });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.qualidadeSono).toEqual(qualidadeSono);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualidadeSono>>();
      const qualidadeSono = { id: 123 };
      jest.spyOn(qualidadeSonoFormService, 'getQualidadeSono').mockReturnValue(qualidadeSono);
      jest.spyOn(qualidadeSonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualidadeSono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qualidadeSono }));
      saveSubject.complete();

      // THEN
      expect(qualidadeSonoFormService.getQualidadeSono).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(qualidadeSonoService.update).toHaveBeenCalledWith(expect.objectContaining(qualidadeSono));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualidadeSono>>();
      const qualidadeSono = { id: 123 };
      jest.spyOn(qualidadeSonoFormService, 'getQualidadeSono').mockReturnValue({ id: null });
      jest.spyOn(qualidadeSonoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualidadeSono: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qualidadeSono }));
      saveSubject.complete();

      // THEN
      expect(qualidadeSonoFormService.getQualidadeSono).toHaveBeenCalled();
      expect(qualidadeSonoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualidadeSono>>();
      const qualidadeSono = { id: 123 };
      jest.spyOn(qualidadeSonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualidadeSono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(qualidadeSonoService.update).toHaveBeenCalled();
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
