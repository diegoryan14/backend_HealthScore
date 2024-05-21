import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnuncio, NewAnuncio } from '../anuncio.model';

export type PartialUpdateAnuncio = Partial<IAnuncio> & Pick<IAnuncio, 'id'>;

type RestOf<T extends IAnuncio | NewAnuncio> = Omit<T, 'dataPublicacao' | 'dataInicio' | 'dataFim'> & {
  dataPublicacao?: string | null;
  dataInicio?: string | null;
  dataFim?: string | null;
};

export type RestAnuncio = RestOf<IAnuncio>;

export type NewRestAnuncio = RestOf<NewAnuncio>;

export type PartialUpdateRestAnuncio = RestOf<PartialUpdateAnuncio>;

export type EntityResponseType = HttpResponse<IAnuncio>;
export type EntityArrayResponseType = HttpResponse<IAnuncio[]>;

@Injectable({ providedIn: 'root' })
export class AnuncioService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/anuncios');

  create(anuncio: NewAnuncio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anuncio);
    return this.http
      .post<RestAnuncio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(anuncio: IAnuncio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anuncio);
    return this.http
      .put<RestAnuncio>(`${this.resourceUrl}/${this.getAnuncioIdentifier(anuncio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(anuncio: PartialUpdateAnuncio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anuncio);
    return this.http
      .patch<RestAnuncio>(`${this.resourceUrl}/${this.getAnuncioIdentifier(anuncio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAnuncio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAnuncio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAnuncioIdentifier(anuncio: Pick<IAnuncio, 'id'>): number {
    return anuncio.id;
  }

  compareAnuncio(o1: Pick<IAnuncio, 'id'> | null, o2: Pick<IAnuncio, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnuncioIdentifier(o1) === this.getAnuncioIdentifier(o2) : o1 === o2;
  }

  addAnuncioToCollectionIfMissing<Type extends Pick<IAnuncio, 'id'>>(
    anuncioCollection: Type[],
    ...anunciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const anuncios: Type[] = anunciosToCheck.filter(isPresent);
    if (anuncios.length > 0) {
      const anuncioCollectionIdentifiers = anuncioCollection.map(anuncioItem => this.getAnuncioIdentifier(anuncioItem));
      const anunciosToAdd = anuncios.filter(anuncioItem => {
        const anuncioIdentifier = this.getAnuncioIdentifier(anuncioItem);
        if (anuncioCollectionIdentifiers.includes(anuncioIdentifier)) {
          return false;
        }
        anuncioCollectionIdentifiers.push(anuncioIdentifier);
        return true;
      });
      return [...anunciosToAdd, ...anuncioCollection];
    }
    return anuncioCollection;
  }

  protected convertDateFromClient<T extends IAnuncio | NewAnuncio | PartialUpdateAnuncio>(anuncio: T): RestOf<T> {
    return {
      ...anuncio,
      dataPublicacao: anuncio.dataPublicacao?.toJSON() ?? null,
      dataInicio: anuncio.dataInicio?.toJSON() ?? null,
      dataFim: anuncio.dataFim?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAnuncio: RestAnuncio): IAnuncio {
    return {
      ...restAnuncio,
      dataPublicacao: restAnuncio.dataPublicacao ? dayjs(restAnuncio.dataPublicacao) : undefined,
      dataInicio: restAnuncio.dataInicio ? dayjs(restAnuncio.dataInicio) : undefined,
      dataFim: restAnuncio.dataFim ? dayjs(restAnuncio.dataFim) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAnuncio>): HttpResponse<IAnuncio> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAnuncio[]>): HttpResponse<IAnuncio[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
