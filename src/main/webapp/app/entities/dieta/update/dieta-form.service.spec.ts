import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dieta.test-samples';

import { DietaFormService } from './dieta-form.service';

describe('Dieta Form Service', () => {
  let service: DietaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DietaFormService);
  });

  describe('Service methods', () => {
    describe('createDietaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDietaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricaoRefeicao: expect.any(Object),
            dataHorarioRefeicao: expect.any(Object),
            caloriasConsumidas: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });

      it('passing IDieta should create a new form with FormGroup', () => {
        const formGroup = service.createDietaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricaoRefeicao: expect.any(Object),
            dataHorarioRefeicao: expect.any(Object),
            caloriasConsumidas: expect.any(Object),
            internalUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getDieta', () => {
      it('should return NewDieta for default Dieta initial value', () => {
        const formGroup = service.createDietaFormGroup(sampleWithNewData);

        const dieta = service.getDieta(formGroup) as any;

        expect(dieta).toMatchObject(sampleWithNewData);
      });

      it('should return NewDieta for empty Dieta initial value', () => {
        const formGroup = service.createDietaFormGroup();

        const dieta = service.getDieta(formGroup) as any;

        expect(dieta).toMatchObject({});
      });

      it('should return IDieta', () => {
        const formGroup = service.createDietaFormGroup(sampleWithRequiredData);

        const dieta = service.getDieta(formGroup) as any;

        expect(dieta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDieta should not enable id FormControl', () => {
        const formGroup = service.createDietaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDieta should disable id FormControl', () => {
        const formGroup = service.createDietaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
