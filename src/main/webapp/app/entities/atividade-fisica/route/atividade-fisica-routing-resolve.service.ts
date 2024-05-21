import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAtividadeFisica } from '../atividade-fisica.model';
import { AtividadeFisicaService } from '../service/atividade-fisica.service';

const atividadeFisicaResolve = (route: ActivatedRouteSnapshot): Observable<null | IAtividadeFisica> => {
  const id = route.params['id'];
  if (id) {
    return inject(AtividadeFisicaService)
      .find(id)
      .pipe(
        mergeMap((atividadeFisica: HttpResponse<IAtividadeFisica>) => {
          if (atividadeFisica.body) {
            return of(atividadeFisica.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default atividadeFisicaResolve;
