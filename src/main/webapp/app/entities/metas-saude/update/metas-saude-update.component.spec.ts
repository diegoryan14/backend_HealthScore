import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { MetasSaudeService } from '../service/metas-saude.service';
import { IMetasSaude } from '../metas-saude.model';
import { MetasSaudeFormService } from './metas-saude-form.service';

import { MetasSaudeUpdateComponent } from './metas-saude-update.component';

describe('MetasSaude Management Update Component', () => {
  let comp: MetasSaudeUpdateComponent;
  let fixture: ComponentFixture<MetasSaudeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metasSaudeFormService: MetasSaudeFormService;
  let metasSaudeService: MetasSaudeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MetasSaudeUpdateComponent],
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
      .overrideTemplate(MetasSaudeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetasSaudeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metasSaudeFormService = TestBed.inject(MetasSaudeFormService);
    metasSaudeService = TestBed.inject(MetasSaudeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const metasSaude: IMetasSaude = { id: 456 };
      const internalUser: IUser = { id: 10243 };
      metasSaude.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 20563 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ metasSaude });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const metasSaude: IMetasSaude = { id: 456 };
      const internalUser: IUser = { id: 14686 };
      metasSaude.internalUser = internalUser;

      activatedRoute.data = of({ metasSaude });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.metasSaude).toEqual(metasSaude);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetasSaude>>();
      const metasSaude = { id: 123 };
      jest.spyOn(metasSaudeFormService, 'getMetasSaude').mockReturnValue(metasSaude);
      jest.spyOn(metasSaudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metasSaude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metasSaude }));
      saveSubject.complete();

      // THEN
      expect(metasSaudeFormService.getMetasSaude).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metasSaudeService.update).toHaveBeenCalledWith(expect.objectContaining(metasSaude));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetasSaude>>();
      const metasSaude = { id: 123 };
      jest.spyOn(metasSaudeFormService, 'getMetasSaude').mockReturnValue({ id: null });
      jest.spyOn(metasSaudeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metasSaude: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metasSaude }));
      saveSubject.complete();

      // THEN
      expect(metasSaudeFormService.getMetasSaude).toHaveBeenCalled();
      expect(metasSaudeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetasSaude>>();
      const metasSaude = { id: 123 };
      jest.spyOn(metasSaudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metasSaude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metasSaudeService.update).toHaveBeenCalled();
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
