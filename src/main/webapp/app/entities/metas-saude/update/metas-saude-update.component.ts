import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { TipoMeta } from 'app/entities/enumerations/tipo-meta.model';
import { MetasSaudeService } from '../service/metas-saude.service';
import { IMetasSaude } from '../metas-saude.model';
import { MetasSaudeFormService, MetasSaudeFormGroup } from './metas-saude-form.service';

@Component({
  standalone: true,
  selector: 'app-metas-saude-update',
  templateUrl: './metas-saude-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetasSaudeUpdateComponent implements OnInit {
  isSaving = false;
  metasSaude: IMetasSaude | null = null;
  tipoMetaValues = Object.keys(TipoMeta);

  usersSharedCollection: IUser[] = [];

  protected metasSaudeService = inject(MetasSaudeService);
  protected metasSaudeFormService = inject(MetasSaudeFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetasSaudeFormGroup = this.metasSaudeFormService.createMetasSaudeFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metasSaude }) => {
      this.metasSaude = metasSaude;
      if (metasSaude) {
        this.updateForm(metasSaude);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metasSaude = this.metasSaudeFormService.getMetasSaude(this.editForm);
    if (metasSaude.id !== null) {
      this.subscribeToSaveResponse(this.metasSaudeService.update(metasSaude));
    } else {
      this.subscribeToSaveResponse(this.metasSaudeService.create(metasSaude));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetasSaude>>): void {
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

  protected updateForm(metasSaude: IMetasSaude): void {
    this.metasSaude = metasSaude;
    this.metasSaudeFormService.resetForm(this.editForm, metasSaude);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, metasSaude.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.metasSaude?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
