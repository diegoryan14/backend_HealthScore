import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pontuacao-usuario.test-samples';

import { PontuacaoUsuarioFormService } from './pontuacao-usuario-form.service';

describe('PontuacaoUsuario Form Service', () => {
  let service: PontuacaoUsuarioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PontuacaoUsuarioFormService);
  });

  describe('Service methods', () => {
    describe('createPontuacaoUsuarioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataAlteracao: expect.any(Object),
            valorAlteracao: expect.any(Object),
            motivo: expect.any(Object),
            usuario: expect.any(Object),
          }),
        );
      });

      it('passing IPontuacaoUsuario should create a new form with FormGroup', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataAlteracao: expect.any(Object),
            valorAlteracao: expect.any(Object),
            motivo: expect.any(Object),
            usuario: expect.any(Object),
          }),
        );
      });
    });

    describe('getPontuacaoUsuario', () => {
      it('should return NewPontuacaoUsuario for default PontuacaoUsuario initial value', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup(sampleWithNewData);

        const pontuacaoUsuario = service.getPontuacaoUsuario(formGroup) as any;

        expect(pontuacaoUsuario).toMatchObject(sampleWithNewData);
      });

      it('should return NewPontuacaoUsuario for empty PontuacaoUsuario initial value', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup();

        const pontuacaoUsuario = service.getPontuacaoUsuario(formGroup) as any;

        expect(pontuacaoUsuario).toMatchObject({});
      });

      it('should return IPontuacaoUsuario', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup(sampleWithRequiredData);

        const pontuacaoUsuario = service.getPontuacaoUsuario(formGroup) as any;

        expect(pontuacaoUsuario).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPontuacaoUsuario should not enable id FormControl', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPontuacaoUsuario should disable id FormControl', () => {
        const formGroup = service.createPontuacaoUsuarioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
