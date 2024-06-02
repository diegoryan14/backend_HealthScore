import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPontuacaoUsuario } from '../pontuacao-usuario.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pontuacao-usuario.test-samples';

import { PontuacaoUsuarioService, RestPontuacaoUsuario } from './pontuacao-usuario.service';

const requireRestSample: RestPontuacaoUsuario = {
  ...sampleWithRequiredData,
  dataAlteracao: sampleWithRequiredData.dataAlteracao?.toJSON(),
};

describe('PontuacaoUsuario Service', () => {
  let service: PontuacaoUsuarioService;
  let httpMock: HttpTestingController;
  let expectedResult: IPontuacaoUsuario | IPontuacaoUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PontuacaoUsuarioService);
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

    it('should create a PontuacaoUsuario', () => {
      const pontuacaoUsuario = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pontuacaoUsuario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PontuacaoUsuario', () => {
      const pontuacaoUsuario = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pontuacaoUsuario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PontuacaoUsuario', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PontuacaoUsuario', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PontuacaoUsuario', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPontuacaoUsuarioToCollectionIfMissing', () => {
      it('should add a PontuacaoUsuario to an empty array', () => {
        const pontuacaoUsuario: IPontuacaoUsuario = sampleWithRequiredData;
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing([], pontuacaoUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pontuacaoUsuario);
      });

      it('should not add a PontuacaoUsuario to an array that contains it', () => {
        const pontuacaoUsuario: IPontuacaoUsuario = sampleWithRequiredData;
        const pontuacaoUsuarioCollection: IPontuacaoUsuario[] = [
          {
            ...pontuacaoUsuario,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing(pontuacaoUsuarioCollection, pontuacaoUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PontuacaoUsuario to an array that doesn't contain it", () => {
        const pontuacaoUsuario: IPontuacaoUsuario = sampleWithRequiredData;
        const pontuacaoUsuarioCollection: IPontuacaoUsuario[] = [sampleWithPartialData];
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing(pontuacaoUsuarioCollection, pontuacaoUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pontuacaoUsuario);
      });

      it('should add only unique PontuacaoUsuario to an array', () => {
        const pontuacaoUsuarioArray: IPontuacaoUsuario[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pontuacaoUsuarioCollection: IPontuacaoUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing(pontuacaoUsuarioCollection, ...pontuacaoUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pontuacaoUsuario: IPontuacaoUsuario = sampleWithRequiredData;
        const pontuacaoUsuario2: IPontuacaoUsuario = sampleWithPartialData;
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing([], pontuacaoUsuario, pontuacaoUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pontuacaoUsuario);
        expect(expectedResult).toContain(pontuacaoUsuario2);
      });

      it('should accept null and undefined values', () => {
        const pontuacaoUsuario: IPontuacaoUsuario = sampleWithRequiredData;
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing([], null, pontuacaoUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pontuacaoUsuario);
      });

      it('should return initial array if no PontuacaoUsuario is added', () => {
        const pontuacaoUsuarioCollection: IPontuacaoUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addPontuacaoUsuarioToCollectionIfMissing(pontuacaoUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(pontuacaoUsuarioCollection);
      });
    });

    describe('comparePontuacaoUsuario', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePontuacaoUsuario(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePontuacaoUsuario(entity1, entity2);
        const compareResult2 = service.comparePontuacaoUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePontuacaoUsuario(entity1, entity2);
        const compareResult2 = service.comparePontuacaoUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePontuacaoUsuario(entity1, entity2);
        const compareResult2 = service.comparePontuacaoUsuario(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
