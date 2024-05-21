import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetasSaude } from '../metas-saude.model';
import { MetasSaudeService } from '../service/metas-saude.service';

const metasSaudeResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetasSaude> => {
  const id = route.params['id'];
  if (id) {
    return inject(MetasSaudeService)
      .find(id)
      .pipe(
        mergeMap((metasSaude: HttpResponse<IMetasSaude>) => {
          if (metasSaude.body) {
            return of(metasSaude.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default metasSaudeResolve;
