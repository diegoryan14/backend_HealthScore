import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../anuncio.test-samples';

import { AnuncioFormService } from './anuncio-form.service';

describe('Anuncio Form Service', () => {
  let service: AnuncioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnuncioFormService);
  });

  describe('Service methods', () => {
    describe('createAnuncioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAnuncioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descricao: expect.any(Object),
            dataPublicacao: expect.any(Object),
            dataInicio: expect.any(Object),
            dataFim: expect.any(Object),
            preco: expect.any(Object),
          }),
        );
      });

      it('passing IAnuncio should create a new form with FormGroup', () => {
        const formGroup = service.createAnuncioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descricao: expect.any(Object),
            dataPublicacao: expect.any(Object),
            dataInicio: expect.any(Object),
            dataFim: expect.any(Object),
            preco: expect.any(Object),
          }),
        );
      });
    });

    describe('getAnuncio', () => {
      it('should return NewAnuncio for default Anuncio initial value', () => {
        const formGroup = service.createAnuncioFormGroup(sampleWithNewData);

        const anuncio = service.getAnuncio(formGroup) as any;

        expect(anuncio).toMatchObject(sampleWithNewData);
      });

      it('should return NewAnuncio for empty Anuncio initial value', () => {
        const formGroup = service.createAnuncioFormGroup();

        const anuncio = service.getAnuncio(formGroup) as any;

        expect(anuncio).toMatchObject({});
      });

      it('should return IAnuncio', () => {
        const formGroup = service.createAnuncioFormGroup(sampleWithRequiredData);

        const anuncio = service.getAnuncio(formGroup) as any;

        expect(anuncio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAnuncio should not enable id FormControl', () => {
        const formGroup = service.createAnuncioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAnuncio should disable id FormControl', () => {
        const formGroup = service.createAnuncioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
