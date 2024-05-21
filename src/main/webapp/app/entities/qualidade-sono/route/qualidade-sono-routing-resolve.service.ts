import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQualidadeSono } from '../qualidade-sono.model';
import { QualidadeSonoService } from '../service/qualidade-sono.service';

const qualidadeSonoResolve = (route: ActivatedRouteSnapshot): Observable<null | IQualidadeSono> => {
  const id = route.params['id'];
  if (id) {
    return inject(QualidadeSonoService)
      .find(id)
      .pipe(
        mergeMap((qualidadeSono: HttpResponse<IQualidadeSono>) => {
          if (qualidadeSono.body) {
            return of(qualidadeSono.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default qualidadeSonoResolve;
