import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPontuacaoUsuario } from '../pontuacao-usuario.model';
import { PontuacaoUsuarioService } from '../service/pontuacao-usuario.service';

const pontuacaoUsuarioResolve = (route: ActivatedRouteSnapshot): Observable<null | IPontuacaoUsuario> => {
  const id = route.params['id'];
  if (id) {
    return inject(PontuacaoUsuarioService)
      .find(id)
      .pipe(
        mergeMap((pontuacaoUsuario: HttpResponse<IPontuacaoUsuario>) => {
          if (pontuacaoUsuario.body) {
            return of(pontuacaoUsuario.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pontuacaoUsuarioResolve;
