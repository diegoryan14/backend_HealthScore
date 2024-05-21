import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../metas-saude.test-samples';

import { MetasSaudeFormService } from './metas-saude-form.service';

describe('MetasSaude Form Service', () => {
  let service: MetasSaudeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetasSaudeFormService);
  });

  describe('Service methods', () => {
    describe('createMetasSaudeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetasSaudeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoMeta: expect.any(Object),
            valorMeta: expect.any(Object),
            dataInicio: expect.any(Object),
            dataFim: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IMetasSaude should create a new form with FormGroup', () => {
        const formGroup = service.createMetasSaudeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoMeta: expect.any(Object),
            valorMeta: expect.any(Object),
            dataInicio: expect.any(Object),
            dataFim: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetasSaude', () => {
      it('should return NewMetasSaude for default MetasSaude initial value', () => {
        const formGroup = service.createMetasSaudeFormGroup(sampleWithNewData);

        const metasSaude = service.getMetasSaude(formGroup) as any;

        expect(metasSaude).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetasSaude for empty MetasSaude initial value', () => {
        const formGroup = service.createMetasSaudeFormGroup();

        const metasSaude = service.getMetasSaude(formGroup) as any;

        expect(metasSaude).toMatchObject({});
      });

      it('should return IMetasSaude', () => {
        const formGroup = service.createMetasSaudeFormGroup(sampleWithRequiredData);

        const metasSaude = service.getMetasSaude(formGroup) as any;

        expect(metasSaude).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetasSaude should not enable id FormControl', () => {
        const formGroup = service.createMetasSaudeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetasSaude should disable id FormControl', () => {
        const formGroup = service.createMetasSaudeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
