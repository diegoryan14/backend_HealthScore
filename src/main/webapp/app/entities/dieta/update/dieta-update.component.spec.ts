import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { DietaService } from '../service/dieta.service';
import { IDieta } from '../dieta.model';
import { DietaFormService } from './dieta-form.service';

import { DietaUpdateComponent } from './dieta-update.component';

describe('Dieta Management Update Component', () => {
  let comp: DietaUpdateComponent;
  let fixture: ComponentFixture<DietaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dietaFormService: DietaFormService;
  let dietaService: DietaService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DietaUpdateComponent],
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
      .overrideTemplate(DietaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DietaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dietaFormService = TestBed.inject(DietaFormService);
    dietaService = TestBed.inject(DietaService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const dieta: IDieta = { id: 456 };
      const internalUser: IUser = { id: 27811 };
      dieta.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 12707 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dieta: IDieta = { id: 456 };
      const internalUser: IUser = { id: 22688 };
      dieta.internalUser = internalUser;

      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.dieta).toEqual(dieta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaFormService, 'getDieta').mockReturnValue(dieta);
      jest.spyOn(dietaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dieta }));
      saveSubject.complete();

      // THEN
      expect(dietaFormService.getDieta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dietaService.update).toHaveBeenCalledWith(expect.objectContaining(dieta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaFormService, 'getDieta').mockReturnValue({ id: null });
      jest.spyOn(dietaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dieta }));
      saveSubject.complete();

      // THEN
      expect(dietaFormService.getDieta).toHaveBeenCalled();
      expect(dietaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dietaService.update).toHaveBeenCalled();
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
