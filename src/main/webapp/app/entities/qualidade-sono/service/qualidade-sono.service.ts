import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQualidadeSono, NewQualidadeSono } from '../qualidade-sono.model';

export type PartialUpdateQualidadeSono = Partial<IQualidadeSono> & Pick<IQualidadeSono, 'id'>;

type RestOf<T extends IQualidadeSono | NewQualidadeSono> = Omit<T, 'data'> & {
  data?: string | null;
};

export type RestQualidadeSono = RestOf<IQualidadeSono>;

export type NewRestQualidadeSono = RestOf<NewQualidadeSono>;

export type PartialUpdateRestQualidadeSono = RestOf<PartialUpdateQualidadeSono>;

export type EntityResponseType = HttpResponse<IQualidadeSono>;
export type EntityArrayResponseType = HttpResponse<IQualidadeSono[]>;

@Injectable({ providedIn: 'root' })
export class QualidadeSonoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/qualidade-sonos');

  create(qualidadeSono: NewQualidadeSono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(qualidadeSono);
    return this.http
      .post<RestQualidadeSono>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(qualidadeSono: IQualidadeSono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(qualidadeSono);
    return this.http
      .put<RestQualidadeSono>(`${this.resourceUrl}/${this.getQualidadeSonoIdentifier(qualidadeSono)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(qualidadeSono: PartialUpdateQualidadeSono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(qualidadeSono);
    return this.http
      .patch<RestQualidadeSono>(`${this.resourceUrl}/${this.getQualidadeSonoIdentifier(qualidadeSono)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQualidadeSono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQualidadeSono[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQualidadeSonoIdentifier(qualidadeSono: Pick<IQualidadeSono, 'id'>): number {
    return qualidadeSono.id;
  }

  compareQualidadeSono(o1: Pick<IQualidadeSono, 'id'> | null, o2: Pick<IQualidadeSono, 'id'> | null): boolean {
    return o1 && o2 ? this.getQualidadeSonoIdentifier(o1) === this.getQualidadeSonoIdentifier(o2) : o1 === o2;
  }

  addQualidadeSonoToCollectionIfMissing<Type extends Pick<IQualidadeSono, 'id'>>(
    qualidadeSonoCollection: Type[],
    ...qualidadeSonosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const qualidadeSonos: Type[] = qualidadeSonosToCheck.filter(isPresent);
    if (qualidadeSonos.length > 0) {
      const qualidadeSonoCollectionIdentifiers = qualidadeSonoCollection.map(qualidadeSonoItem =>
        this.getQualidadeSonoIdentifier(qualidadeSonoItem),
      );
      const qualidadeSonosToAdd = qualidadeSonos.filter(qualidadeSonoItem => {
        const qualidadeSonoIdentifier = this.getQualidadeSonoIdentifier(qualidadeSonoItem);
        if (qualidadeSonoCollectionIdentifiers.includes(qualidadeSonoIdentifier)) {
          return false;
        }
        qualidadeSonoCollectionIdentifiers.push(qualidadeSonoIdentifier);
        return true;
      });
      return [...qualidadeSonosToAdd, ...qualidadeSonoCollection];
    }
    return qualidadeSonoCollection;
  }

  protected convertDateFromClient<T extends IQualidadeSono | NewQualidadeSono | PartialUpdateQualidadeSono>(qualidadeSono: T): RestOf<T> {
    return {
      ...qualidadeSono,
      data: qualidadeSono.data?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQualidadeSono: RestQualidadeSono): IQualidadeSono {
    return {
      ...restQualidadeSono,
      data: restQualidadeSono.data ? dayjs(restQualidadeSono.data) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQualidadeSono>): HttpResponse<IQualidadeSono> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQualidadeSono[]>): HttpResponse<IQualidadeSono[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
