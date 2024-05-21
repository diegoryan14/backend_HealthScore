import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { AtividadeFisicaService } from '../service/atividade-fisica.service';
import { IAtividadeFisica } from '../atividade-fisica.model';
import { AtividadeFisicaFormService } from './atividade-fisica-form.service';

import { AtividadeFisicaUpdateComponent } from './atividade-fisica-update.component';

describe('AtividadeFisica Management Update Component', () => {
  let comp: AtividadeFisicaUpdateComponent;
  let fixture: ComponentFixture<AtividadeFisicaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let atividadeFisicaFormService: AtividadeFisicaFormService;
  let atividadeFisicaService: AtividadeFisicaService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AtividadeFisicaUpdateComponent],
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
      .overrideTemplate(AtividadeFisicaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtividadeFisicaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    atividadeFisicaFormService = TestBed.inject(AtividadeFisicaFormService);
    atividadeFisicaService = TestBed.inject(AtividadeFisicaService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const atividadeFisica: IAtividadeFisica = { id: 456 };
      const internalUser: IUser = { id: 11939 };
      atividadeFisica.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 9978 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ atividadeFisica });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const atividadeFisica: IAtividadeFisica = { id: 456 };
      const internalUser: IUser = { id: 287 };
      atividadeFisica.internalUser = internalUser;

      activatedRoute.data = of({ atividadeFisica });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.atividadeFisica).toEqual(atividadeFisica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtividadeFisica>>();
      const atividadeFisica = { id: 123 };
      jest.spyOn(atividadeFisicaFormService, 'getAtividadeFisica').mockReturnValue(atividadeFisica);
      jest.spyOn(atividadeFisicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atividadeFisica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atividadeFisica }));
      saveSubject.complete();

      // THEN
      expect(atividadeFisicaFormService.getAtividadeFisica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(atividadeFisicaService.update).toHaveBeenCalledWith(expect.objectContaining(atividadeFisica));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtividadeFisica>>();
      const atividadeFisica = { id: 123 };
      jest.spyOn(atividadeFisicaFormService, 'getAtividadeFisica').mockReturnValue({ id: null });
      jest.spyOn(atividadeFisicaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atividadeFisica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atividadeFisica }));
      saveSubject.complete();

      // THEN
      expect(atividadeFisicaFormService.getAtividadeFisica).toHaveBeenCalled();
      expect(atividadeFisicaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtividadeFisica>>();
      const atividadeFisica = { id: 123 };
      jest.spyOn(atividadeFisicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atividadeFisica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(atividadeFisicaService.update).toHaveBeenCalled();
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
