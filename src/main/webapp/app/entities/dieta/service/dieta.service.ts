import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDieta, NewDieta } from '../dieta.model';

export type PartialUpdateDieta = Partial<IDieta> & Pick<IDieta, 'id'>;

type RestOf<T extends IDieta | NewDieta> = Omit<T, 'dataHorarioRefeicao'> & {
  dataHorarioRefeicao?: string | null;
};

export type RestDieta = RestOf<IDieta>;

export type NewRestDieta = RestOf<NewDieta>;

export type PartialUpdateRestDieta = RestOf<PartialUpdateDieta>;

export type EntityResponseType = HttpResponse<IDieta>;
export type EntityArrayResponseType = HttpResponse<IDieta[]>;

@Injectable({ providedIn: 'root' })
export class DietaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dietas');

  create(dieta: NewDieta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dieta);
    return this.http.post<RestDieta>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dieta: IDieta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dieta);
    return this.http
      .put<RestDieta>(`${this.resourceUrl}/${this.getDietaIdentifier(dieta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dieta: PartialUpdateDieta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dieta);
    return this.http
      .patch<RestDieta>(`${this.resourceUrl}/${this.getDietaIdentifier(dieta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDieta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDieta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDietaIdentifier(dieta: Pick<IDieta, 'id'>): number {
    return dieta.id;
  }

  compareDieta(o1: Pick<IDieta, 'id'> | null, o2: Pick<IDieta, 'id'> | null): boolean {
    return o1 && o2 ? this.getDietaIdentifier(o1) === this.getDietaIdentifier(o2) : o1 === o2;
  }

  addDietaToCollectionIfMissing<Type extends Pick<IDieta, 'id'>>(
    dietaCollection: Type[],
    ...dietasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dietas: Type[] = dietasToCheck.filter(isPresent);
    if (dietas.length > 0) {
      const dietaCollectionIdentifiers = dietaCollection.map(dietaItem => this.getDietaIdentifier(dietaItem));
      const dietasToAdd = dietas.filter(dietaItem => {
        const dietaIdentifier = this.getDietaIdentifier(dietaItem);
        if (dietaCollectionIdentifiers.includes(dietaIdentifier)) {
          return false;
        }
        dietaCollectionIdentifiers.push(dietaIdentifier);
        return true;
      });
      return [...dietasToAdd, ...dietaCollection];
    }
    return dietaCollection;
  }

  protected convertDateFromClient<T extends IDieta | NewDieta | PartialUpdateDieta>(dieta: T): RestOf<T> {
    return {
      ...dieta,
      dataHorarioRefeicao: dieta.dataHorarioRefeicao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDieta: RestDieta): IDieta {
    return {
      ...restDieta,
      dataHorarioRefeicao: restDieta.dataHorarioRefeicao ? dayjs(restDieta.dataHorarioRefeicao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDieta>): HttpResponse<IDieta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDieta[]>): HttpResponse<IDieta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
