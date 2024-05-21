import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDieta } from '../dieta.model';
import { DietaService } from '../service/dieta.service';
import { DietaFormService, DietaFormGroup } from './dieta-form.service';

@Component({
  standalone: true,
  selector: 'app-dieta-update',
  templateUrl: './dieta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DietaUpdateComponent implements OnInit {
  isSaving = false;
  dieta: IDieta | null = null;

  usersSharedCollection: IUser[] = [];

  protected dietaService = inject(DietaService);
  protected dietaFormService = inject(DietaFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DietaFormGroup = this.dietaFormService.createDietaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dieta }) => {
      this.dieta = dieta;
      if (dieta) {
        this.updateForm(dieta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dieta = this.dietaFormService.getDieta(this.editForm);
    if (dieta.id !== null) {
      this.subscribeToSaveResponse(this.dietaService.update(dieta));
    } else {
      this.subscribeToSaveResponse(this.dietaService.create(dieta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDieta>>): void {
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

  protected updateForm(dieta: IDieta): void {
    this.dieta = dieta;
    this.dietaFormService.resetForm(this.editForm, dieta);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, dieta.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.dieta?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
