import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../consulta-especialista.test-samples';

import { ConsultaEspecialistaFormService } from './consulta-especialista-form.service';

describe('ConsultaEspecialista Form Service', () => {
  let service: ConsultaEspecialistaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsultaEspecialistaFormService);
  });

  describe('Service methods', () => {
    describe('createConsultaEspecialistaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoEspecialista: expect.any(Object),
            dataHorarioConsulta: expect.any(Object),
            statusConsulta: expect.any(Object),
            linkConsulta: expect.any(Object),
            internalUser: expect.any(Object),
            especialista: expect.any(Object),
          }),
        );
      });

      it('passing IConsultaEspecialista should create a new form with FormGroup', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoEspecialista: expect.any(Object),
            dataHorarioConsulta: expect.any(Object),
            statusConsulta: expect.any(Object),
            linkConsulta: expect.any(Object),
            internalUser: expect.any(Object),
            especialista: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsultaEspecialista', () => {
      it('should return NewConsultaEspecialista for default ConsultaEspecialista initial value', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup(sampleWithNewData);

        const consultaEspecialista = service.getConsultaEspecialista(formGroup) as any;

        expect(consultaEspecialista).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsultaEspecialista for empty ConsultaEspecialista initial value', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup();

        const consultaEspecialista = service.getConsultaEspecialista(formGroup) as any;

        expect(consultaEspecialista).toMatchObject({});
      });

      it('should return IConsultaEspecialista', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup(sampleWithRequiredData);

        const consultaEspecialista = service.getConsultaEspecialista(formGroup) as any;

        expect(consultaEspecialista).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsultaEspecialista should not enable id FormControl', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsultaEspecialista should disable id FormControl', () => {
        const formGroup = service.createConsultaEspecialistaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
