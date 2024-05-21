import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IControleMedicamentos } from '../controle-medicamentos.model';
import { ControleMedicamentosService } from '../service/controle-medicamentos.service';

const controleMedicamentosResolve = (route: ActivatedRouteSnapshot): Observable<null | IControleMedicamentos> => {
  const id = route.params['id'];
  if (id) {
    return inject(ControleMedicamentosService)
      .find(id)
      .pipe(
        mergeMap((controleMedicamentos: HttpResponse<IControleMedicamentos>) => {
          if (controleMedicamentos.body) {
            return of(controleMedicamentos.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default controleMedicamentosResolve;
