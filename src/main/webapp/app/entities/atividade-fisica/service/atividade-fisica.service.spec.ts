import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAtividadeFisica } from '../atividade-fisica.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../atividade-fisica.test-samples';

import { AtividadeFisicaService, RestAtividadeFisica } from './atividade-fisica.service';

const requireRestSample: RestAtividadeFisica = {
  ...sampleWithRequiredData,
  dataHorario: sampleWithRequiredData.dataHorario?.toJSON(),
};

describe('AtividadeFisica Service', () => {
  let service: AtividadeFisicaService;
  let httpMock: HttpTestingController;
  let expectedResult: IAtividadeFisica | IAtividadeFisica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AtividadeFisicaService);
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

    it('should create a AtividadeFisica', () => {
      const atividadeFisica = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(atividadeFisica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AtividadeFisica', () => {
      const atividadeFisica = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(atividadeFisica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AtividadeFisica', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AtividadeFisica', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AtividadeFisica', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAtividadeFisicaToCollectionIfMissing', () => {
      it('should add a AtividadeFisica to an empty array', () => {
        const atividadeFisica: IAtividadeFisica = sampleWithRequiredData;
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing([], atividadeFisica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atividadeFisica);
      });

      it('should not add a AtividadeFisica to an array that contains it', () => {
        const atividadeFisica: IAtividadeFisica = sampleWithRequiredData;
        const atividadeFisicaCollection: IAtividadeFisica[] = [
          {
            ...atividadeFisica,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing(atividadeFisicaCollection, atividadeFisica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AtividadeFisica to an array that doesn't contain it", () => {
        const atividadeFisica: IAtividadeFisica = sampleWithRequiredData;
        const atividadeFisicaCollection: IAtividadeFisica[] = [sampleWithPartialData];
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing(atividadeFisicaCollection, atividadeFisica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atividadeFisica);
      });

      it('should add only unique AtividadeFisica to an array', () => {
        const atividadeFisicaArray: IAtividadeFisica[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const atividadeFisicaCollection: IAtividadeFisica[] = [sampleWithRequiredData];
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing(atividadeFisicaCollection, ...atividadeFisicaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const atividadeFisica: IAtividadeFisica = sampleWithRequiredData;
        const atividadeFisica2: IAtividadeFisica = sampleWithPartialData;
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing([], atividadeFisica, atividadeFisica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atividadeFisica);
        expect(expectedResult).toContain(atividadeFisica2);
      });

      it('should accept null and undefined values', () => {
        const atividadeFisica: IAtividadeFisica = sampleWithRequiredData;
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing([], null, atividadeFisica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atividadeFisica);
      });

      it('should return initial array if no AtividadeFisica is added', () => {
        const atividadeFisicaCollection: IAtividadeFisica[] = [sampleWithRequiredData];
        expectedResult = service.addAtividadeFisicaToCollectionIfMissing(atividadeFisicaCollection, undefined, null);
        expect(expectedResult).toEqual(atividadeFisicaCollection);
      });
    });

    describe('compareAtividadeFisica', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAtividadeFisica(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAtividadeFisica(entity1, entity2);
        const compareResult2 = service.compareAtividadeFisica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAtividadeFisica(entity1, entity2);
        const compareResult2 = service.compareAtividadeFisica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAtividadeFisica(entity1, entity2);
        const compareResult2 = service.compareAtividadeFisica(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
