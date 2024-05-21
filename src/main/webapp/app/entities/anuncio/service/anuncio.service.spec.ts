import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnuncio } from '../anuncio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../anuncio.test-samples';

import { AnuncioService, RestAnuncio } from './anuncio.service';

const requireRestSample: RestAnuncio = {
  ...sampleWithRequiredData,
  dataPublicacao: sampleWithRequiredData.dataPublicacao?.toJSON(),
  dataInicio: sampleWithRequiredData.dataInicio?.toJSON(),
  dataFim: sampleWithRequiredData.dataFim?.toJSON(),
};

describe('Anuncio Service', () => {
  let service: AnuncioService;
  let httpMock: HttpTestingController;
  let expectedResult: IAnuncio | IAnuncio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnuncioService);
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

    it('should create a Anuncio', () => {
      const anuncio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(anuncio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Anuncio', () => {
      const anuncio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(anuncio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Anuncio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Anuncio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Anuncio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAnuncioToCollectionIfMissing', () => {
      it('should add a Anuncio to an empty array', () => {
        const anuncio: IAnuncio = sampleWithRequiredData;
        expectedResult = service.addAnuncioToCollectionIfMissing([], anuncio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anuncio);
      });

      it('should not add a Anuncio to an array that contains it', () => {
        const anuncio: IAnuncio = sampleWithRequiredData;
        const anuncioCollection: IAnuncio[] = [
          {
            ...anuncio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAnuncioToCollectionIfMissing(anuncioCollection, anuncio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Anuncio to an array that doesn't contain it", () => {
        const anuncio: IAnuncio = sampleWithRequiredData;
        const anuncioCollection: IAnuncio[] = [sampleWithPartialData];
        expectedResult = service.addAnuncioToCollectionIfMissing(anuncioCollection, anuncio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anuncio);
      });

      it('should add only unique Anuncio to an array', () => {
        const anuncioArray: IAnuncio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const anuncioCollection: IAnuncio[] = [sampleWithRequiredData];
        expectedResult = service.addAnuncioToCollectionIfMissing(anuncioCollection, ...anuncioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const anuncio: IAnuncio = sampleWithRequiredData;
        const anuncio2: IAnuncio = sampleWithPartialData;
        expectedResult = service.addAnuncioToCollectionIfMissing([], anuncio, anuncio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anuncio);
        expect(expectedResult).toContain(anuncio2);
      });

      it('should accept null and undefined values', () => {
        const anuncio: IAnuncio = sampleWithRequiredData;
        expectedResult = service.addAnuncioToCollectionIfMissing([], null, anuncio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anuncio);
      });

      it('should return initial array if no Anuncio is added', () => {
        const anuncioCollection: IAnuncio[] = [sampleWithRequiredData];
        expectedResult = service.addAnuncioToCollectionIfMissing(anuncioCollection, undefined, null);
        expect(expectedResult).toEqual(anuncioCollection);
      });
    });

    describe('compareAnuncio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAnuncio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAnuncio(entity1, entity2);
        const compareResult2 = service.compareAnuncio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAnuncio(entity1, entity2);
        const compareResult2 = service.compareAnuncio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAnuncio(entity1, entity2);
        const compareResult2 = service.compareAnuncio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
