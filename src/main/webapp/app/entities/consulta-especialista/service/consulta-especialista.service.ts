import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsultaEspecialista, NewConsultaEspecialista } from '../consulta-especialista.model';

export type PartialUpdateConsultaEspecialista = Partial<IConsultaEspecialista> & Pick<IConsultaEspecialista, 'id'>;

type RestOf<T extends IConsultaEspecialista | NewConsultaEspecialista> = Omit<T, 'dataHorarioConsulta'> & {
  dataHorarioConsulta?: string | null;
};

export type RestConsultaEspecialista = RestOf<IConsultaEspecialista>;

export type NewRestConsultaEspecialista = RestOf<NewConsultaEspecialista>;

export type PartialUpdateRestConsultaEspecialista = RestOf<PartialUpdateConsultaEspecialista>;

export type EntityResponseType = HttpResponse<IConsultaEspecialista>;
export type EntityArrayResponseType = HttpResponse<IConsultaEspecialista[]>;

@Injectable({ providedIn: 'root' })
export class ConsultaEspecialistaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consulta-especialistas');

  create(consultaEspecialista: NewConsultaEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultaEspecialista);
    return this.http
      .post<RestConsultaEspecialista>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(consultaEspecialista: IConsultaEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultaEspecialista);
    return this.http
      .put<RestConsultaEspecialista>(`${this.resourceUrl}/${this.getConsultaEspecialistaIdentifier(consultaEspecialista)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(consultaEspecialista: PartialUpdateConsultaEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultaEspecialista);
    return this.http
      .patch<RestConsultaEspecialista>(`${this.resourceUrl}/${this.getConsultaEspecialistaIdentifier(consultaEspecialista)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConsultaEspecialista>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConsultaEspecialista[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsultaEspecialistaIdentifier(consultaEspecialista: Pick<IConsultaEspecialista, 'id'>): number {
    return consultaEspecialista.id;
  }

  compareConsultaEspecialista(o1: Pick<IConsultaEspecialista, 'id'> | null, o2: Pick<IConsultaEspecialista, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsultaEspecialistaIdentifier(o1) === this.getConsultaEspecialistaIdentifier(o2) : o1 === o2;
  }

  addConsultaEspecialistaToCollectionIfMissing<Type extends Pick<IConsultaEspecialista, 'id'>>(
    consultaEspecialistaCollection: Type[],
    ...consultaEspecialistasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consultaEspecialistas: Type[] = consultaEspecialistasToCheck.filter(isPresent);
    if (consultaEspecialistas.length > 0) {
      const consultaEspecialistaCollectionIdentifiers = consultaEspecialistaCollection.map(consultaEspecialistaItem =>
        this.getConsultaEspecialistaIdentifier(consultaEspecialistaItem),
      );
      const consultaEspecialistasToAdd = consultaEspecialistas.filter(consultaEspecialistaItem => {
        const consultaEspecialistaIdentifier = this.getConsultaEspecialistaIdentifier(consultaEspecialistaItem);
        if (consultaEspecialistaCollectionIdentifiers.includes(consultaEspecialistaIdentifier)) {
          return false;
        }
        consultaEspecialistaCollectionIdentifiers.push(consultaEspecialistaIdentifier);
        return true;
      });
      return [...consultaEspecialistasToAdd, ...consultaEspecialistaCollection];
    }
    return consultaEspecialistaCollection;
  }

  protected convertDateFromClient<T extends IConsultaEspecialista | NewConsultaEspecialista | PartialUpdateConsultaEspecialista>(
    consultaEspecialista: T,
  ): RestOf<T> {
    return {
      ...consultaEspecialista,
      dataHorarioConsulta: consultaEspecialista.dataHorarioConsulta?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restConsultaEspecialista: RestConsultaEspecialista): IConsultaEspecialista {
    return {
      ...restConsultaEspecialista,
      dataHorarioConsulta: restConsultaEspecialista.dataHorarioConsulta ? dayjs(restConsultaEspecialista.dataHorarioConsulta) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConsultaEspecialista>): HttpResponse<IConsultaEspecialista> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConsultaEspecialista[]>): HttpResponse<IConsultaEspecialista[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
