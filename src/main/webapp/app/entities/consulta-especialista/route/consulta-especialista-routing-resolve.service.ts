import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsultaEspecialista } from '../consulta-especialista.model';
import { ConsultaEspecialistaService } from '../service/consulta-especialista.service';

const consultaEspecialistaResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsultaEspecialista> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsultaEspecialistaService)
      .find(id)
      .pipe(
        mergeMap((consultaEspecialista: HttpResponse<IConsultaEspecialista>) => {
          if (consultaEspecialista.body) {
            return of(consultaEspecialista.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consultaEspecialistaResolve;
