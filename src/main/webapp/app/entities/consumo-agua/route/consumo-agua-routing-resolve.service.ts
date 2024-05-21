import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsumoAgua } from '../consumo-agua.model';
import { ConsumoAguaService } from '../service/consumo-agua.service';

const consumoAguaResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsumoAgua> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsumoAguaService)
      .find(id)
      .pipe(
        mergeMap((consumoAgua: HttpResponse<IConsumoAgua>) => {
          if (consumoAgua.body) {
            return of(consumoAgua.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consumoAguaResolve;
