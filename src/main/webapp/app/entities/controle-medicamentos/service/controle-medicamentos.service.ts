import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IControleMedicamentos, NewControleMedicamentos } from '../controle-medicamentos.model';

export type PartialUpdateControleMedicamentos = Partial<IControleMedicamentos> & Pick<IControleMedicamentos, 'id'>;

type RestOf<T extends IControleMedicamentos | NewControleMedicamentos> = Omit<T, 'horarioIngestao'> & {
  horarioIngestao?: string | null;
};

export type RestControleMedicamentos = RestOf<IControleMedicamentos>;

export type NewRestControleMedicamentos = RestOf<NewControleMedicamentos>;

export type PartialUpdateRestControleMedicamentos = RestOf<PartialUpdateControleMedicamentos>;

export type EntityResponseType = HttpResponse<IControleMedicamentos>;
export type EntityArrayResponseType = HttpResponse<IControleMedicamentos[]>;

@Injectable({ providedIn: 'root' })
export class ControleMedicamentosService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/controle-medicamentos');

  create(controleMedicamentos: NewControleMedicamentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controleMedicamentos);
    return this.http
      .post<RestControleMedicamentos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(controleMedicamentos: IControleMedicamentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controleMedicamentos);
    return this.http
      .put<RestControleMedicamentos>(`${this.resourceUrl}/${this.getControleMedicamentosIdentifier(controleMedicamentos)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(controleMedicamentos: PartialUpdateControleMedicamentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controleMedicamentos);
    return this.http
      .patch<RestControleMedicamentos>(`${this.resourceUrl}/${this.getControleMedicamentosIdentifier(controleMedicamentos)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestControleMedicamentos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestControleMedicamentos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getControleMedicamentosIdentifier(controleMedicamentos: Pick<IControleMedicamentos, 'id'>): number {
    return controleMedicamentos.id;
  }

  compareControleMedicamentos(o1: Pick<IControleMedicamentos, 'id'> | null, o2: Pick<IControleMedicamentos, 'id'> | null): boolean {
    return o1 && o2 ? this.getControleMedicamentosIdentifier(o1) === this.getControleMedicamentosIdentifier(o2) : o1 === o2;
  }

  addControleMedicamentosToCollectionIfMissing<Type extends Pick<IControleMedicamentos, 'id'>>(
    controleMedicamentosCollection: Type[],
    ...controleMedicamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const controleMedicamentos: Type[] = controleMedicamentosToCheck.filter(isPresent);
    if (controleMedicamentos.length > 0) {
      const controleMedicamentosCollectionIdentifiers = controleMedicamentosCollection.map(controleMedicamentosItem =>
        this.getControleMedicamentosIdentifier(controleMedicamentosItem),
      );
      const controleMedicamentosToAdd = controleMedicamentos.filter(controleMedicamentosItem => {
        const controleMedicamentosIdentifier = this.getControleMedicamentosIdentifier(controleMedicamentosItem);
        if (controleMedicamentosCollectionIdentifiers.includes(controleMedicamentosIdentifier)) {
          return false;
        }
        controleMedicamentosCollectionIdentifiers.push(controleMedicamentosIdentifier);
        return true;
      });
      return [...controleMedicamentosToAdd, ...controleMedicamentosCollection];
    }
    return controleMedicamentosCollection;
  }

  protected convertDateFromClient<T extends IControleMedicamentos | NewControleMedicamentos | PartialUpdateControleMedicamentos>(
    controleMedicamentos: T,
  ): RestOf<T> {
    return {
      ...controleMedicamentos,
      horarioIngestao: controleMedicamentos.horarioIngestao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restControleMedicamentos: RestControleMedicamentos): IControleMedicamentos {
    return {
      ...restControleMedicamentos,
      horarioIngestao: restControleMedicamentos.horarioIngestao ? dayjs(restControleMedicamentos.horarioIngestao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestControleMedicamentos>): HttpResponse<IControleMedicamentos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestControleMedicamentos[]>): HttpResponse<IControleMedicamentos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
