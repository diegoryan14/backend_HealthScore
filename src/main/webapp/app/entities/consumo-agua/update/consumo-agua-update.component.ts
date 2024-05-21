import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IConsumoAgua } from '../consumo-agua.model';
import { ConsumoAguaService } from '../service/consumo-agua.service';
import { ConsumoAguaFormService, ConsumoAguaFormGroup } from './consumo-agua-form.service';

@Component({
  standalone: true,
  selector: 'app-consumo-agua-update',
  templateUrl: './consumo-agua-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsumoAguaUpdateComponent implements OnInit {
  isSaving = false;
  consumoAgua: IConsumoAgua | null = null;

  usersSharedCollection: IUser[] = [];

  protected consumoAguaService = inject(ConsumoAguaService);
  protected consumoAguaFormService = inject(ConsumoAguaFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConsumoAguaFormGroup = this.consumoAguaFormService.createConsumoAguaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumoAgua }) => {
      this.consumoAgua = consumoAgua;
      if (consumoAgua) {
        this.updateForm(consumoAgua);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumoAgua = this.consumoAguaFormService.getConsumoAgua(this.editForm);
    if (consumoAgua.id !== null) {
      this.subscribeToSaveResponse(this.consumoAguaService.update(consumoAgua));
    } else {
      this.subscribeToSaveResponse(this.consumoAguaService.create(consumoAgua));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumoAgua>>): void {
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

  protected updateForm(consumoAgua: IConsumoAgua): void {
    this.consumoAgua = consumoAgua;
    this.consumoAguaFormService.resetForm(this.editForm, consumoAgua);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, consumoAgua.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.consumoAgua?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
