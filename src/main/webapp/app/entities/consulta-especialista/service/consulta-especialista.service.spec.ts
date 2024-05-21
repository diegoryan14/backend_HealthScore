import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConsultaEspecialista } from '../consulta-especialista.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../consulta-especialista.test-samples';

import { ConsultaEspecialistaService, RestConsultaEspecialista } from './consulta-especialista.service';

const requireRestSample: RestConsultaEspecialista = {
  ...sampleWithRequiredData,
  dataHorarioConsulta: sampleWithRequiredData.dataHorarioConsulta?.toJSON(),
};

describe('ConsultaEspecialista Service', () => {
  let service: ConsultaEspecialistaService;
  let httpMock: HttpTestingController;
  let expectedResult: IConsultaEspecialista | IConsultaEspecialista[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConsultaEspecialistaService);
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

    it('should create a ConsultaEspecialista', () => {
      const consultaEspecialista = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(consultaEspecialista).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConsultaEspecialista', () => {
      const consultaEspecialista = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(consultaEspecialista).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConsultaEspecialista', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConsultaEspecialista', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConsultaEspecialista', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConsultaEspecialistaToCollectionIfMissing', () => {
      it('should add a ConsultaEspecialista to an empty array', () => {
        const consultaEspecialista: IConsultaEspecialista = sampleWithRequiredData;
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing([], consultaEspecialista);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consultaEspecialista);
      });

      it('should not add a ConsultaEspecialista to an array that contains it', () => {
        const consultaEspecialista: IConsultaEspecialista = sampleWithRequiredData;
        const consultaEspecialistaCollection: IConsultaEspecialista[] = [
          {
            ...consultaEspecialista,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing(consultaEspecialistaCollection, consultaEspecialista);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConsultaEspecialista to an array that doesn't contain it", () => {
        const consultaEspecialista: IConsultaEspecialista = sampleWithRequiredData;
        const consultaEspecialistaCollection: IConsultaEspecialista[] = [sampleWithPartialData];
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing(consultaEspecialistaCollection, consultaEspecialista);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consultaEspecialista);
      });

      it('should add only unique ConsultaEspecialista to an array', () => {
        const consultaEspecialistaArray: IConsultaEspecialista[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const consultaEspecialistaCollection: IConsultaEspecialista[] = [sampleWithRequiredData];
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing(consultaEspecialistaCollection, ...consultaEspecialistaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const consultaEspecialista: IConsultaEspecialista = sampleWithRequiredData;
        const consultaEspecialista2: IConsultaEspecialista = sampleWithPartialData;
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing([], consultaEspecialista, consultaEspecialista2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consultaEspecialista);
        expect(expectedResult).toContain(consultaEspecialista2);
      });

      it('should accept null and undefined values', () => {
        const consultaEspecialista: IConsultaEspecialista = sampleWithRequiredData;
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing([], null, consultaEspecialista, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consultaEspecialista);
      });

      it('should return initial array if no ConsultaEspecialista is added', () => {
        const consultaEspecialistaCollection: IConsultaEspecialista[] = [sampleWithRequiredData];
        expectedResult = service.addConsultaEspecialistaToCollectionIfMissing(consultaEspecialistaCollection, undefined, null);
        expect(expectedResult).toEqual(consultaEspecialistaCollection);
      });
    });

    describe('compareConsultaEspecialista', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConsultaEspecialista(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConsultaEspecialista(entity1, entity2);
        const compareResult2 = service.compareConsultaEspecialista(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConsultaEspecialista(entity1, entity2);
        const compareResult2 = service.compareConsultaEspecialista(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConsultaEspecialista(entity1, entity2);
        const compareResult2 = service.compareConsultaEspecialista(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
