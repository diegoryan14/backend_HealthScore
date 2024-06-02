import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPontuacaoUsuario, NewPontuacaoUsuario } from '../pontuacao-usuario.model';

export type PartialUpdatePontuacaoUsuario = Partial<IPontuacaoUsuario> & Pick<IPontuacaoUsuario, 'id'>;

type RestOf<T extends IPontuacaoUsuario | NewPontuacaoUsuario> = Omit<T, 'dataAlteracao'> & {
  dataAlteracao?: string | null;
};

export type RestPontuacaoUsuario = RestOf<IPontuacaoUsuario>;

export type NewRestPontuacaoUsuario = RestOf<NewPontuacaoUsuario>;

export type PartialUpdateRestPontuacaoUsuario = RestOf<PartialUpdatePontuacaoUsuario>;

export type EntityResponseType = HttpResponse<IPontuacaoUsuario>;
export type EntityArrayResponseType = HttpResponse<IPontuacaoUsuario[]>;

@Injectable({ providedIn: 'root' })
export class PontuacaoUsuarioService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pontuacao-usuarios');

  create(pontuacaoUsuario: NewPontuacaoUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pontuacaoUsuario);
    return this.http
      .post<RestPontuacaoUsuario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pontuacaoUsuario: IPontuacaoUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pontuacaoUsuario);
    return this.http
      .put<RestPontuacaoUsuario>(`${this.resourceUrl}/${this.getPontuacaoUsuarioIdentifier(pontuacaoUsuario)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pontuacaoUsuario: PartialUpdatePontuacaoUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pontuacaoUsuario);
    return this.http
      .patch<RestPontuacaoUsuario>(`${this.resourceUrl}/${this.getPontuacaoUsuarioIdentifier(pontuacaoUsuario)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPontuacaoUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPontuacaoUsuario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPontuacaoUsuarioIdentifier(pontuacaoUsuario: Pick<IPontuacaoUsuario, 'id'>): number {
    return pontuacaoUsuario.id;
  }

  comparePontuacaoUsuario(o1: Pick<IPontuacaoUsuario, 'id'> | null, o2: Pick<IPontuacaoUsuario, 'id'> | null): boolean {
    return o1 && o2 ? this.getPontuacaoUsuarioIdentifier(o1) === this.getPontuacaoUsuarioIdentifier(o2) : o1 === o2;
  }

  addPontuacaoUsuarioToCollectionIfMissing<Type extends Pick<IPontuacaoUsuario, 'id'>>(
    pontuacaoUsuarioCollection: Type[],
    ...pontuacaoUsuariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pontuacaoUsuarios: Type[] = pontuacaoUsuariosToCheck.filter(isPresent);
    if (pontuacaoUsuarios.length > 0) {
      const pontuacaoUsuarioCollectionIdentifiers = pontuacaoUsuarioCollection.map(pontuacaoUsuarioItem =>
        this.getPontuacaoUsuarioIdentifier(pontuacaoUsuarioItem),
      );
      const pontuacaoUsuariosToAdd = pontuacaoUsuarios.filter(pontuacaoUsuarioItem => {
        const pontuacaoUsuarioIdentifier = this.getPontuacaoUsuarioIdentifier(pontuacaoUsuarioItem);
        if (pontuacaoUsuarioCollectionIdentifiers.includes(pontuacaoUsuarioIdentifier)) {
          return false;
        }
        pontuacaoUsuarioCollectionIdentifiers.push(pontuacaoUsuarioIdentifier);
        return true;
      });
      return [...pontuacaoUsuariosToAdd, ...pontuacaoUsuarioCollection];
    }
    return pontuacaoUsuarioCollection;
  }

  protected convertDateFromClient<T extends IPontuacaoUsuario | NewPontuacaoUsuario | PartialUpdatePontuacaoUsuario>(
    pontuacaoUsuario: T,
  ): RestOf<T> {
    return {
      ...pontuacaoUsuario,
      dataAlteracao: pontuacaoUsuario.dataAlteracao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPontuacaoUsuario: RestPontuacaoUsuario): IPontuacaoUsuario {
    return {
      ...restPontuacaoUsuario,
      dataAlteracao: restPontuacaoUsuario.dataAlteracao ? dayjs(restPontuacaoUsuario.dataAlteracao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPontuacaoUsuario>): HttpResponse<IPontuacaoUsuario> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPontuacaoUsuario[]>): HttpResponse<IPontuacaoUsuario[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
