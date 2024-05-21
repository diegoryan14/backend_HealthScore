import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEspecialista, NewEspecialista } from '../especialista.model';

export type PartialUpdateEspecialista = Partial<IEspecialista> & Pick<IEspecialista, 'id'>;

type RestOf<T extends IEspecialista | NewEspecialista> = Omit<T, 'dataFormacao' | 'dataNascimento'> & {
  dataFormacao?: string | null;
  dataNascimento?: string | null;
};

export type RestEspecialista = RestOf<IEspecialista>;

export type NewRestEspecialista = RestOf<NewEspecialista>;

export type PartialUpdateRestEspecialista = RestOf<PartialUpdateEspecialista>;

export type EntityResponseType = HttpResponse<IEspecialista>;
export type EntityArrayResponseType = HttpResponse<IEspecialista[]>;

@Injectable({ providedIn: 'root' })
export class EspecialistaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/especialistas');

  create(especialista: NewEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(especialista);
    return this.http
      .post<RestEspecialista>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(especialista: IEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(especialista);
    return this.http
      .put<RestEspecialista>(`${this.resourceUrl}/${this.getEspecialistaIdentifier(especialista)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(especialista: PartialUpdateEspecialista): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(especialista);
    return this.http
      .patch<RestEspecialista>(`${this.resourceUrl}/${this.getEspecialistaIdentifier(especialista)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEspecialista>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEspecialista[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEspecialistaIdentifier(especialista: Pick<IEspecialista, 'id'>): number {
    return especialista.id;
  }

  compareEspecialista(o1: Pick<IEspecialista, 'id'> | null, o2: Pick<IEspecialista, 'id'> | null): boolean {
    return o1 && o2 ? this.getEspecialistaIdentifier(o1) === this.getEspecialistaIdentifier(o2) : o1 === o2;
  }

  addEspecialistaToCollectionIfMissing<Type extends Pick<IEspecialista, 'id'>>(
    especialistaCollection: Type[],
    ...especialistasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const especialistas: Type[] = especialistasToCheck.filter(isPresent);
    if (especialistas.length > 0) {
      const especialistaCollectionIdentifiers = especialistaCollection.map(especialistaItem =>
        this.getEspecialistaIdentifier(especialistaItem),
      );
      const especialistasToAdd = especialistas.filter(especialistaItem => {
        const especialistaIdentifier = this.getEspecialistaIdentifier(especialistaItem);
        if (especialistaCollectionIdentifiers.includes(especialistaIdentifier)) {
          return false;
        }
        especialistaCollectionIdentifiers.push(especialistaIdentifier);
        return true;
      });
      return [...especialistasToAdd, ...especialistaCollection];
    }
    return especialistaCollection;
  }

  protected convertDateFromClient<T extends IEspecialista | NewEspecialista | PartialUpdateEspecialista>(especialista: T): RestOf<T> {
    return {
      ...especialista,
      dataFormacao: especialista.dataFormacao?.toJSON() ?? null,
      dataNascimento: especialista.dataNascimento?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEspecialista: RestEspecialista): IEspecialista {
    return {
      ...restEspecialista,
      dataFormacao: restEspecialista.dataFormacao ? dayjs(restEspecialista.dataFormacao) : undefined,
      dataNascimento: restEspecialista.dataNascimento ? dayjs(restEspecialista.dataNascimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEspecialista>): HttpResponse<IEspecialista> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEspecialista[]>): HttpResponse<IEspecialista[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
