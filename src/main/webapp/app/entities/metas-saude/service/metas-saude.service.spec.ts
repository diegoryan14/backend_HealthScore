import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMetasSaude } from '../metas-saude.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../metas-saude.test-samples';

import { MetasSaudeService, RestMetasSaude } from './metas-saude.service';

const requireRestSample: RestMetasSaude = {
  ...sampleWithRequiredData,
  dataInicio: sampleWithRequiredData.dataInicio?.toJSON(),
  dataFim: sampleWithRequiredData.dataFim?.toJSON(),
};

describe('MetasSaude Service', () => {
  let service: MetasSaudeService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetasSaude | IMetasSaude[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MetasSaudeService);
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

    it('should create a MetasSaude', () => {
      const metasSaude = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metasSaude).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetasSaude', () => {
      const metasSaude = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metasSaude).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetasSaude', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetasSaude', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetasSaude', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMetasSaudeToCollectionIfMissing', () => {
      it('should add a MetasSaude to an empty array', () => {
        const metasSaude: IMetasSaude = sampleWithRequiredData;
        expectedResult = service.addMetasSaudeToCollectionIfMissing([], metasSaude);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metasSaude);
      });

      it('should not add a MetasSaude to an array that contains it', () => {
        const metasSaude: IMetasSaude = sampleWithRequiredData;
        const metasSaudeCollection: IMetasSaude[] = [
          {
            ...metasSaude,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetasSaudeToCollectionIfMissing(metasSaudeCollection, metasSaude);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetasSaude to an array that doesn't contain it", () => {
        const metasSaude: IMetasSaude = sampleWithRequiredData;
        const metasSaudeCollection: IMetasSaude[] = [sampleWithPartialData];
        expectedResult = service.addMetasSaudeToCollectionIfMissing(metasSaudeCollection, metasSaude);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metasSaude);
      });

      it('should add only unique MetasSaude to an array', () => {
        const metasSaudeArray: IMetasSaude[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metasSaudeCollection: IMetasSaude[] = [sampleWithRequiredData];
        expectedResult = service.addMetasSaudeToCollectionIfMissing(metasSaudeCollection, ...metasSaudeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metasSaude: IMetasSaude = sampleWithRequiredData;
        const metasSaude2: IMetasSaude = sampleWithPartialData;
        expectedResult = service.addMetasSaudeToCollectionIfMissing([], metasSaude, metasSaude2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metasSaude);
        expect(expectedResult).toContain(metasSaude2);
      });

      it('should accept null and undefined values', () => {
        const metasSaude: IMetasSaude = sampleWithRequiredData;
        expectedResult = service.addMetasSaudeToCollectionIfMissing([], null, metasSaude, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metasSaude);
      });

      it('should return initial array if no MetasSaude is added', () => {
        const metasSaudeCollection: IMetasSaude[] = [sampleWithRequiredData];
        expectedResult = service.addMetasSaudeToCollectionIfMissing(metasSaudeCollection, undefined, null);
        expect(expectedResult).toEqual(metasSaudeCollection);
      });
    });

    describe('compareMetasSaude', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetasSaude(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMetasSaude(entity1, entity2);
        const compareResult2 = service.compareMetasSaude(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMetasSaude(entity1, entity2);
        const compareResult2 = service.compareMetasSaude(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMetasSaude(entity1, entity2);
        const compareResult2 = service.compareMetasSaude(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
