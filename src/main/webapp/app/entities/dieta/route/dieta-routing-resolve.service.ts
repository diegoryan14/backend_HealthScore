import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDieta } from '../dieta.model';
import { DietaService } from '../service/dieta.service';

const dietaResolve = (route: ActivatedRouteSnapshot): Observable<null | IDieta> => {
  const id = route.params['id'];
  if (id) {
    return inject(DietaService)
      .find(id)
      .pipe(
        mergeMap((dieta: HttpResponse<IDieta>) => {
          if (dieta.body) {
            return of(dieta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default dietaResolve;
