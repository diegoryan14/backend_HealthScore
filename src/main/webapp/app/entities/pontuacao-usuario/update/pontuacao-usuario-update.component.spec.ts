import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { PontuacaoUsuarioService } from '../service/pontuacao-usuario.service';
import { IPontuacaoUsuario } from '../pontuacao-usuario.model';
import { PontuacaoUsuarioFormService } from './pontuacao-usuario-form.service';

import { PontuacaoUsuarioUpdateComponent } from './pontuacao-usuario-update.component';

describe('PontuacaoUsuario Management Update Component', () => {
  let comp: PontuacaoUsuarioUpdateComponent;
  let fixture: ComponentFixture<PontuacaoUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pontuacaoUsuarioFormService: PontuacaoUsuarioFormService;
  let pontuacaoUsuarioService: PontuacaoUsuarioService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PontuacaoUsuarioUpdateComponent],
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
      .overrideTemplate(PontuacaoUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PontuacaoUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pontuacaoUsuarioFormService = TestBed.inject(PontuacaoUsuarioFormService);
    pontuacaoUsuarioService = TestBed.inject(PontuacaoUsuarioService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const pontuacaoUsuario: IPontuacaoUsuario = { id: 456 };
      const usuario: IUsuario = { id: 7060 };
      pontuacaoUsuario.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 27402 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pontuacaoUsuario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(expect.objectContaining),
      );
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pontuacaoUsuario: IPontuacaoUsuario = { id: 456 };
      const usuario: IUsuario = { id: 2416 };
      pontuacaoUsuario.usuario = usuario;

      activatedRoute.data = of({ pontuacaoUsuario });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.pontuacaoUsuario).toEqual(pontuacaoUsuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPontuacaoUsuario>>();
      const pontuacaoUsuario = { id: 123 };
      jest.spyOn(pontuacaoUsuarioFormService, 'getPontuacaoUsuario').mockReturnValue(pontuacaoUsuario);
      jest.spyOn(pontuacaoUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pontuacaoUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pontuacaoUsuario }));
      saveSubject.complete();

      // THEN
      expect(pontuacaoUsuarioFormService.getPontuacaoUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pontuacaoUsuarioService.update).toHaveBeenCalledWith(expect.objectContaining(pontuacaoUsuario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPontuacaoUsuario>>();
      const pontuacaoUsuario = { id: 123 };
      jest.spyOn(pontuacaoUsuarioFormService, 'getPontuacaoUsuario').mockReturnValue({ id: null });
      jest.spyOn(pontuacaoUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pontuacaoUsuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pontuacaoUsuario }));
      saveSubject.complete();

      // THEN
      expect(pontuacaoUsuarioFormService.getPontuacaoUsuario).toHaveBeenCalled();
      expect(pontuacaoUsuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPontuacaoUsuario>>();
      const pontuacaoUsuario = { id: 123 };
      jest.spyOn(pontuacaoUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pontuacaoUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pontuacaoUsuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUsuario', () => {
      it('Should forward to usuarioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
