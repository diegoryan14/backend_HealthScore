import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../qualidade-sono.test-samples';

import { QualidadeSonoFormService } from './qualidade-sono-form.service';

describe('QualidadeSono Form Service', () => {
  let service: QualidadeSonoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QualidadeSonoFormService);
  });

  describe('Service methods', () => {
    describe('createQualidadeSonoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQualidadeSonoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            data: expect.any(Object),
            horasSono: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IQualidadeSono should create a new form with FormGroup', () => {
        const formGroup = service.createQualidadeSonoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            data: expect.any(Object),
            horasSono: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getQualidadeSono', () => {
      it('should return NewQualidadeSono for default QualidadeSono initial value', () => {
        const formGroup = service.createQualidadeSonoFormGroup(sampleWithNewData);

        const qualidadeSono = service.getQualidadeSono(formGroup) as any;

        expect(qualidadeSono).toMatchObject(sampleWithNewData);
      });

      it('should return NewQualidadeSono for empty QualidadeSono initial value', () => {
        const formGroup = service.createQualidadeSonoFormGroup();

        const qualidadeSono = service.getQualidadeSono(formGroup) as any;

        expect(qualidadeSono).toMatchObject({});
      });

      it('should return IQualidadeSono', () => {
        const formGroup = service.createQualidadeSonoFormGroup(sampleWithRequiredData);

        const qualidadeSono = service.getQualidadeSono(formGroup) as any;

        expect(qualidadeSono).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQualidadeSono should not enable id FormControl', () => {
        const formGroup = service.createQualidadeSonoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQualidadeSono should disable id FormControl', () => {
        const formGroup = service.createQualidadeSonoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
