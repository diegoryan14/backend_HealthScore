import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IControleMedicamentos } from '../controle-medicamentos.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../controle-medicamentos.test-samples';

import { ControleMedicamentosService, RestControleMedicamentos } from './controle-medicamentos.service';

const requireRestSample: RestControleMedicamentos = {
  ...sampleWithRequiredData,
  horarioIngestao: sampleWithRequiredData.horarioIngestao?.toJSON(),
};

describe('ControleMedicamentos Service', () => {
  let service: ControleMedicamentosService;
  let httpMock: HttpTestingController;
  let expectedResult: IControleMedicamentos | IControleMedicamentos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ControleMedicamentosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ControleMedicamentos', () => {
      const controleMedicamentos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(controleMedicamentos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ControleMedicamentos', () => {
      const controleMedicamentos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(controleMedicamentos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ControleMedicamentos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ControleMedicamentos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ControleMedicamentos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addControleMedicamentosToCollectionIfMissing', () => {
      it('should add a ControleMedicamentos to an empty array', () => {
        const controleMedicamentos: IControleMedicamentos = sampleWithRequiredData;
        expectedResult = service.addControleMedicamentosToCollectionIfMissing([], controleMedicamentos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(controleMedicamentos);
      });

      it('should not add a ControleMedicamentos to an array that contains it', () => {
        const controleMedicamentos: IControleMedicamentos = sampleWithRequiredData;
        const controleMedicamentosCollection: IControleMedicamentos[] = [
          {
            ...controleMedicamentos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addControleMedicamentosToCollectionIfMissing(controleMedicamentosCollection, controleMedicamentos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ControleMedicamentos to an array that doesn't contain it", () => {
        const controleMedicamentos: IControleMedicamentos = sampleWithRequiredData;
        const controleMedicamentosCollection: IControleMedicamentos[] = [sampleWithPartialData];
        expectedResult = service.addControleMedicamentosToCollectionIfMissing(controleMedicamentosCollection, controleMedicamentos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(controleMedicamentos);
      });

      it('should add only unique ControleMedicamentos to an array', () => {
        const controleMedicamentosArray: IControleMedicamentos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const controleMedicamentosCollection: IControleMedicamentos[] = [sampleWithRequiredData];
        expectedResult = service.addControleMedicamentosToCollectionIfMissing(controleMedicamentosCollection, ...controleMedicamentosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const controleMedicamentos: IControleMedicamentos = sampleWithRequiredData;
        const controleMedicamentos2: IControleMedicamentos = sampleWithPartialData;
        expectedResult = service.addControleMedicamentosToCollectionIfMissing([], controleMedicamentos, controleMedicamentos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(controleMedicamentos);
        expect(expectedResult).toContain(controleMedicamentos2);
      });

      it('should accept null and undefined values', () => {
        const controleMedicamentos: IControleMedicamentos = sampleWithRequiredData;
        expectedResult = service.addControleMedicamentosToCollectionIfMissing([], null, controleMedicamentos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(controleMedicamentos);
      });

      it('should return initial array if no ControleMedicamentos is added', () => {
        const controleMedicamentosCollection: IControleMedicamentos[] = [sampleWithRequiredData];
        expectedResult = service.addControleMedicamentosToCollectionIfMissing(controleMedicamentosCollection, undefined, null);
        expect(expectedResult).toEqual(controleMedicamentosCollection);
      });
    });

    describe('compareControleMedicamentos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareControleMedicamentos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareControleMedicamentos(entity1, entity2);
        const compareResult2 = service.compareControleMedicamentos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareControleMedicamentos(entity1, entity2);
        const compareResult2 = service.compareControleMedicamentos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareControleMedicamentos(entity1, entity2);
        const compareResult2 = service.compareControleMedicamentos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
