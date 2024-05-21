import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumoAgua, NewConsumoAgua } from '../consumo-agua.model';

export type PartialUpdateConsumoAgua = Partial<IConsumoAgua> & Pick<IConsumoAgua, 'id'>;

type RestOf<T extends IConsumoAgua | NewConsumoAgua> = Omit<T, 'dataConsumo'> & {
  dataConsumo?: string | null;
};

export type RestConsumoAgua = RestOf<IConsumoAgua>;

export type NewRestConsumoAgua = RestOf<NewConsumoAgua>;

export type PartialUpdateRestConsumoAgua = RestOf<PartialUpdateConsumoAgua>;

export type EntityResponseType = HttpResponse<IConsumoAgua>;
export type EntityArrayResponseType = HttpResponse<IConsumoAgua[]>;

@Injectable({ providedIn: 'root' })
export class ConsumoAguaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consumo-aguas');

  create(consumoAgua: NewConsumoAgua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumoAgua);
    return this.http
      .post<RestConsumoAgua>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(consumoAgua: IConsumoAgua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumoAgua);
    return this.http
      .put<RestConsumoAgua>(`${this.resourceUrl}/${this.getConsumoAguaIdentifier(consumoAgua)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(consumoAgua: PartialUpdateConsumoAgua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumoAgua);
    return this.http
      .patch<RestConsumoAgua>(`${this.resourceUrl}/${this.getConsumoAguaIdentifier(consumoAgua)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConsumoAgua>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConsumoAgua[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsumoAguaIdentifier(consumoAgua: Pick<IConsumoAgua, 'id'>): number {
    return consumoAgua.id;
  }

  compareConsumoAgua(o1: Pick<IConsumoAgua, 'id'> | null, o2: Pick<IConsumoAgua, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsumoAguaIdentifier(o1) === this.getConsumoAguaIdentifier(o2) : o1 === o2;
  }

  addConsumoAguaToCollectionIfMissing<Type extends Pick<IConsumoAgua, 'id'>>(
    consumoAguaCollection: Type[],
    ...consumoAguasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consumoAguas: Type[] = consumoAguasToCheck.filter(isPresent);
    if (consumoAguas.length > 0) {
      const consumoAguaCollectionIdentifiers = consumoAguaCollection.map(consumoAguaItem => this.getConsumoAguaIdentifier(consumoAguaItem));
      const consumoAguasToAdd = consumoAguas.filter(consumoAguaItem => {
        const consumoAguaIdentifier = this.getConsumoAguaIdentifier(consumoAguaItem);
        if (consumoAguaCollectionIdentifiers.includes(consumoAguaIdentifier)) {
          return false;
        }
        consumoAguaCollectionIdentifiers.push(consumoAguaIdentifier);
        return true;
      });
      return [...consumoAguasToAdd, ...consumoAguaCollection];
    }
    return consumoAguaCollection;
  }

  protected convertDateFromClient<T extends IConsumoAgua | NewConsumoAgua | PartialUpdateConsumoAgua>(consumoAgua: T): RestOf<T> {
    return {
      ...consumoAgua,
      dataConsumo: consumoAgua.dataConsumo?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restConsumoAgua: RestConsumoAgua): IConsumoAgua {
    return {
      ...restConsumoAgua,
      dataConsumo: restConsumoAgua.dataConsumo ? dayjs(restConsumoAgua.dataConsumo) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConsumoAgua>): HttpResponse<IConsumoAgua> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConsumoAgua[]>): HttpResponse<IConsumoAgua[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
