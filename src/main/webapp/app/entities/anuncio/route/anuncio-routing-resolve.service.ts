import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnuncio } from '../anuncio.model';
import { AnuncioService } from '../service/anuncio.service';

const anuncioResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnuncio> => {
  const id = route.params['id'];
  if (id) {
    return inject(AnuncioService)
      .find(id)
      .pipe(
        mergeMap((anuncio: HttpResponse<IAnuncio>) => {
          if (anuncio.body) {
            return of(anuncio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default anuncioResolve;
