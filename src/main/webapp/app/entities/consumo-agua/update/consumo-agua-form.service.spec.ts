import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../consumo-agua.test-samples';

import { ConsumoAguaFormService } from './consumo-agua-form.service';

describe('ConsumoAgua Form Service', () => {
  let service: ConsumoAguaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsumoAguaFormService);
  });

  describe('Service methods', () => {
    describe('createConsumoAguaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsumoAguaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataConsumo: expect.any(Object),
            quantidadeMl: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IConsumoAgua should create a new form with FormGroup', () => {
        const formGroup = service.createConsumoAguaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataConsumo: expect.any(Object),
            quantidadeMl: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsumoAgua', () => {
      it('should return NewConsumoAgua for default ConsumoAgua initial value', () => {
        const formGroup = service.createConsumoAguaFormGroup(sampleWithNewData);

        const consumoAgua = service.getConsumoAgua(formGroup) as any;

        expect(consumoAgua).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsumoAgua for empty ConsumoAgua initial value', () => {
        const formGroup = service.createConsumoAguaFormGroup();

        const consumoAgua = service.getConsumoAgua(formGroup) as any;

        expect(consumoAgua).toMatchObject({});
      });

      it('should return IConsumoAgua', () => {
        const formGroup = service.createConsumoAguaFormGroup(sampleWithRequiredData);

        const consumoAgua = service.getConsumoAgua(formGroup) as any;

        expect(consumoAgua).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsumoAgua should not enable id FormControl', () => {
        const formGroup = service.createConsumoAguaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsumoAgua should disable id FormControl', () => {
        const formGroup = service.createConsumoAguaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
