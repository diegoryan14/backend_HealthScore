import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IAtividadeFisica } from '../atividade-fisica.model';
import { AtividadeFisicaService } from '../service/atividade-fisica.service';

import atividadeFisicaResolve from './atividade-fisica-routing-resolve.service';

describe('AtividadeFisica routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AtividadeFisicaService;
  let resultAtividadeFisica: IAtividadeFisica | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(AtividadeFisicaService);
    resultAtividadeFisica = undefined;
  });

  describe('resolve', () => {
    it('should return IAtividadeFisica returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        atividadeFisicaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAtividadeFisica = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAtividadeFisica).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        atividadeFisicaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAtividadeFisica = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAtividadeFisica).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAtividadeFisica>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        atividadeFisicaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAtividadeFisica = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAtividadeFisica).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
