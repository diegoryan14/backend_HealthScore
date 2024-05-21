import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMetasSaude, NewMetasSaude } from '../metas-saude.model';

export type PartialUpdateMetasSaude = Partial<IMetasSaude> & Pick<IMetasSaude, 'id'>;

type RestOf<T extends IMetasSaude | NewMetasSaude> = Omit<T, 'dataInicio' | 'dataFim'> & {
  dataInicio?: string | null;
  dataFim?: string | null;
};

export type RestMetasSaude = RestOf<IMetasSaude>;

export type NewRestMetasSaude = RestOf<NewMetasSaude>;

export type PartialUpdateRestMetasSaude = RestOf<PartialUpdateMetasSaude>;

export type EntityResponseType = HttpResponse<IMetasSaude>;
export type EntityArrayResponseType = HttpResponse<IMetasSaude[]>;

@Injectable({ providedIn: 'root' })
export class MetasSaudeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/metas-saudes');

  create(metasSaude: NewMetasSaude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metasSaude);
    return this.http
      .post<RestMetasSaude>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metasSaude: IMetasSaude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metasSaude);
    return this.http
      .put<RestMetasSaude>(`${this.resourceUrl}/${this.getMetasSaudeIdentifier(metasSaude)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metasSaude: PartialUpdateMetasSaude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metasSaude);
    return this.http
      .patch<RestMetasSaude>(`${this.resourceUrl}/${this.getMetasSaudeIdentifier(metasSaude)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetasSaude>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetasSaude[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMetasSaudeIdentifier(metasSaude: Pick<IMetasSaude, 'id'>): number {
    return metasSaude.id;
  }

  compareMetasSaude(o1: Pick<IMetasSaude, 'id'> | null, o2: Pick<IMetasSaude, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetasSaudeIdentifier(o1) === this.getMetasSaudeIdentifier(o2) : o1 === o2;
  }

  addMetasSaudeToCollectionIfMissing<Type extends Pick<IMetasSaude, 'id'>>(
    metasSaudeCollection: Type[],
    ...metasSaudesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metasSaudes: Type[] = metasSaudesToCheck.filter(isPresent);
    if (metasSaudes.length > 0) {
      const metasSaudeCollectionIdentifiers = metasSaudeCollection.map(metasSaudeItem => this.getMetasSaudeIdentifier(metasSaudeItem));
      const metasSaudesToAdd = metasSaudes.filter(metasSaudeItem => {
        const metasSaudeIdentifier = this.getMetasSaudeIdentifier(metasSaudeItem);
        if (metasSaudeCollectionIdentifiers.includes(metasSaudeIdentifier)) {
          return false;
        }
        metasSaudeCollectionIdentifiers.push(metasSaudeIdentifier);
        return true;
      });
      return [...metasSaudesToAdd, ...metasSaudeCollection];
    }
    return metasSaudeCollection;
  }

  protected convertDateFromClient<T extends IMetasSaude | NewMetasSaude | PartialUpdateMetasSaude>(metasSaude: T): RestOf<T> {
    return {
      ...metasSaude,
      dataInicio: metasSaude.dataInicio?.toJSON() ?? null,
      dataFim: metasSaude.dataFim?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetasSaude: RestMetasSaude): IMetasSaude {
    return {
      ...restMetasSaude,
      dataInicio: restMetasSaude.dataInicio ? dayjs(restMetasSaude.dataInicio) : undefined,
      dataFim: restMetasSaude.dataFim ? dayjs(restMetasSaude.dataFim) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetasSaude>): HttpResponse<IMetasSaude> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetasSaude[]>): HttpResponse<IMetasSaude[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
