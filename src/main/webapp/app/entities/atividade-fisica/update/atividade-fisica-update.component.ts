import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { TipoAtividade } from 'app/entities/enumerations/tipo-atividade.model';
import { AtividadeFisicaService } from '../service/atividade-fisica.service';
import { IAtividadeFisica } from '../atividade-fisica.model';
import { AtividadeFisicaFormService, AtividadeFisicaFormGroup } from './atividade-fisica-form.service';

@Component({
  standalone: true,
  selector: 'app-atividade-fisica-update',
  templateUrl: './atividade-fisica-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AtividadeFisicaUpdateComponent implements OnInit {
  isSaving = false;
  atividadeFisica: IAtividadeFisica | null = null;
  tipoAtividadeValues = Object.keys(TipoAtividade);

  usersSharedCollection: IUser[] = [];

  protected atividadeFisicaService = inject(AtividadeFisicaService);
  protected atividadeFisicaFormService = inject(AtividadeFisicaFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AtividadeFisicaFormGroup = this.atividadeFisicaFormService.createAtividadeFisicaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atividadeFisica }) => {
      this.atividadeFisica = atividadeFisica;
      if (atividadeFisica) {
        this.updateForm(atividadeFisica);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const atividadeFisica = this.atividadeFisicaFormService.getAtividadeFisica(this.editForm);
    if (atividadeFisica.id !== null) {
      this.subscribeToSaveResponse(this.atividadeFisicaService.update(atividadeFisica));
    } else {
      this.subscribeToSaveResponse(this.atividadeFisicaService.create(atividadeFisica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtividadeFisica>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(atividadeFisica: IAtividadeFisica): void {
    this.atividadeFisica = atividadeFisica;
    this.atividadeFisicaFormService.resetForm(this.editForm, atividadeFisica);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      atividadeFisica.internalUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.atividadeFisica?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
