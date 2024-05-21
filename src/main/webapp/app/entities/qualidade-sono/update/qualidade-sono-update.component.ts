import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IQualidadeSono } from '../qualidade-sono.model';
import { QualidadeSonoService } from '../service/qualidade-sono.service';
import { QualidadeSonoFormService, QualidadeSonoFormGroup } from './qualidade-sono-form.service';

@Component({
  standalone: true,
  selector: 'app-qualidade-sono-update',
  templateUrl: './qualidade-sono-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QualidadeSonoUpdateComponent implements OnInit {
  isSaving = false;
  qualidadeSono: IQualidadeSono | null = null;

  usersSharedCollection: IUser[] = [];

  protected qualidadeSonoService = inject(QualidadeSonoService);
  protected qualidadeSonoFormService = inject(QualidadeSonoFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QualidadeSonoFormGroup = this.qualidadeSonoFormService.createQualidadeSonoFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qualidadeSono }) => {
      this.qualidadeSono = qualidadeSono;
      if (qualidadeSono) {
        this.updateForm(qualidadeSono);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const qualidadeSono = this.qualidadeSonoFormService.getQualidadeSono(this.editForm);
    if (qualidadeSono.id !== null) {
      this.subscribeToSaveResponse(this.qualidadeSonoService.update(qualidadeSono));
    } else {
      this.subscribeToSaveResponse(this.qualidadeSonoService.create(qualidadeSono));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQualidadeSono>>): void {
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

  protected updateForm(qualidadeSono: IQualidadeSono): void {
    this.qualidadeSono = qualidadeSono;
    this.qualidadeSonoFormService.resetForm(this.editForm, qualidadeSono);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      qualidadeSono.internalUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.qualidadeSono?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
