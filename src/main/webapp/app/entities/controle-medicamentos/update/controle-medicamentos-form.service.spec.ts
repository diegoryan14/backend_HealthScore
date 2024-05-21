import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../controle-medicamentos.test-samples';

import { ControleMedicamentosFormService } from './controle-medicamentos-form.service';

describe('ControleMedicamentos Form Service', () => {
  let service: ControleMedicamentosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ControleMedicamentosFormService);
  });

  describe('Service methods', () => {
    describe('createControleMedicamentosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createControleMedicamentosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeMedicamento: expect.any(Object),
            dosagem: expect.any(Object),
            horarioIngestao: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IControleMedicamentos should create a new form with FormGroup', () => {
        const formGroup = service.createControleMedicamentosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeMedicamento: expect.any(Object),
            dosagem: expect.any(Object),
            horarioIngestao: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getControleMedicamentos', () => {
      it('should return NewControleMedicamentos for default ControleMedicamentos initial value', () => {
        const formGroup = service.createControleMedicamentosFormGroup(sampleWithNewData);

        const controleMedicamentos = service.getControleMedicamentos(formGroup) as any;

        expect(controleMedicamentos).toMatchObject(sampleWithNewData);
      });

      it('should return NewControleMedicamentos for empty ControleMedicamentos initial value', () => {
        const formGroup = service.createControleMedicamentosFormGroup();

        const controleMedicamentos = service.getControleMedicamentos(formGroup) as any;

        expect(controleMedicamentos).toMatchObject({});
      });

      it('should return IControleMedicamentos', () => {
        const formGroup = service.createControleMedicamentosFormGroup(sampleWithRequiredData);

        const controleMedicamentos = service.getControleMedicamentos(formGroup) as any;

        expect(controleMedicamentos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IControleMedicamentos should not enable id FormControl', () => {
        const formGroup = service.createControleMedicamentosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewControleMedicamentos should disable id FormControl', () => {
        const formGroup = service.createControleMedicamentosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
