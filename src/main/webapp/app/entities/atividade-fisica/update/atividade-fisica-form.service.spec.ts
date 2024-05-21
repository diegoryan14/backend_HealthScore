import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../atividade-fisica.test-samples';

import { AtividadeFisicaFormService } from './atividade-fisica-form.service';

describe('AtividadeFisica Form Service', () => {
  let service: AtividadeFisicaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AtividadeFisicaFormService);
  });

  describe('Service methods', () => {
    describe('createAtividadeFisicaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAtividadeFisicaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoAtividade: expect.any(Object),
            dataHorario: expect.any(Object),
            duracao: expect.any(Object),
            passosCalorias: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IAtividadeFisica should create a new form with FormGroup', () => {
        const formGroup = service.createAtividadeFisicaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoAtividade: expect.any(Object),
            dataHorario: expect.any(Object),
            duracao: expect.any(Object),
            passosCalorias: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getAtividadeFisica', () => {
      it('should return NewAtividadeFisica for default AtividadeFisica initial value', () => {
        const formGroup = service.createAtividadeFisicaFormGroup(sampleWithNewData);

        const atividadeFisica = service.getAtividadeFisica(formGroup) as any;

        expect(atividadeFisica).toMatchObject(sampleWithNewData);
      });

      it('should return NewAtividadeFisica for empty AtividadeFisica initial value', () => {
        const formGroup = service.createAtividadeFisicaFormGroup();

        const atividadeFisica = service.getAtividadeFisica(formGroup) as any;

        expect(atividadeFisica).toMatchObject({});
      });

      it('should return IAtividadeFisica', () => {
        const formGroup = service.createAtividadeFisicaFormGroup(sampleWithRequiredData);

        const atividadeFisica = service.getAtividadeFisica(formGroup) as any;

        expect(atividadeFisica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAtividadeFisica should not enable id FormControl', () => {
        const formGroup = service.createAtividadeFisicaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAtividadeFisica should disable id FormControl', () => {
        const formGroup = service.createAtividadeFisicaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
