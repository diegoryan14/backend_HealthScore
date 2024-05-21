import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAtividadeFisica, NewAtividadeFisica } from '../atividade-fisica.model';

export type PartialUpdateAtividadeFisica = Partial<IAtividadeFisica> & Pick<IAtividadeFisica, 'id'>;

type RestOf<T extends IAtividadeFisica | NewAtividadeFisica> = Omit<T, 'dataHorario'> & {
  dataHorario?: string | null;
};

export type RestAtividadeFisica = RestOf<IAtividadeFisica>;

export type NewRestAtividadeFisica = RestOf<NewAtividadeFisica>;

export type PartialUpdateRestAtividadeFisica = RestOf<PartialUpdateAtividadeFisica>;

export type EntityResponseType = HttpResponse<IAtividadeFisica>;
export type EntityArrayResponseType = HttpResponse<IAtividadeFisica[]>;

@Injectable({ providedIn: 'root' })
export class AtividadeFisicaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/atividade-fisicas');

  create(atividadeFisica: NewAtividadeFisica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atividadeFisica);
    return this.http
      .post<RestAtividadeFisica>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(atividadeFisica: IAtividadeFisica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atividadeFisica);
    return this.http
      .put<RestAtividadeFisica>(`${this.resourceUrl}/${this.getAtividadeFisicaIdentifier(atividadeFisica)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(atividadeFisica: PartialUpdateAtividadeFisica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atividadeFisica);
    return this.http
      .patch<RestAtividadeFisica>(`${this.resourceUrl}/${this.getAtividadeFisicaIdentifier(atividadeFisica)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAtividadeFisica>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAtividadeFisica[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAtividadeFisicaIdentifier(atividadeFisica: Pick<IAtividadeFisica, 'id'>): number {
    return atividadeFisica.id;
  }

  compareAtividadeFisica(o1: Pick<IAtividadeFisica, 'id'> | null, o2: Pick<IAtividadeFisica, 'id'> | null): boolean {
    return o1 && o2 ? this.getAtividadeFisicaIdentifier(o1) === this.getAtividadeFisicaIdentifier(o2) : o1 === o2;
  }

  addAtividadeFisicaToCollectionIfMissing<Type extends Pick<IAtividadeFisica, 'id'>>(
    atividadeFisicaCollection: Type[],
    ...atividadeFisicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const atividadeFisicas: Type[] = atividadeFisicasToCheck.filter(isPresent);
    if (atividadeFisicas.length > 0) {
      const atividadeFisicaCollectionIdentifiers = atividadeFisicaCollection.map(atividadeFisicaItem =>
        this.getAtividadeFisicaIdentifier(atividadeFisicaItem),
      );
      const atividadeFisicasToAdd = atividadeFisicas.filter(atividadeFisicaItem => {
        const atividadeFisicaIdentifier = this.getAtividadeFisicaIdentifier(atividadeFisicaItem);
        if (atividadeFisicaCollectionIdentifiers.includes(atividadeFisicaIdentifier)) {
          return false;
        }
        atividadeFisicaCollectionIdentifiers.push(atividadeFisicaIdentifier);
        return true;
      });
      return [...atividadeFisicasToAdd, ...atividadeFisicaCollection];
    }
    return atividadeFisicaCollection;
  }

  protected convertDateFromClient<T extends IAtividadeFisica | NewAtividadeFisica | PartialUpdateAtividadeFisica>(
    atividadeFisica: T,
  ): RestOf<T> {
    return {
      ...atividadeFisica,
      dataHorario: atividadeFisica.dataHorario?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAtividadeFisica: RestAtividadeFisica): IAtividadeFisica {
    return {
      ...restAtividadeFisica,
      dataHorario: restAtividadeFisica.dataHorario ? dayjs(restAtividadeFisica.dataHorario) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAtividadeFisica>): HttpResponse<IAtividadeFisica> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAtividadeFisica[]>): HttpResponse<IAtividadeFisica[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
