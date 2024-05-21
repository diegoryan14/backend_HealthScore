import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IControleMedicamentos } from '../controle-medicamentos.model';
import { ControleMedicamentosService } from '../service/controle-medicamentos.service';
import { ControleMedicamentosFormService, ControleMedicamentosFormGroup } from './controle-medicamentos-form.service';

@Component({
  standalone: true,
  selector: 'app-controle-medicamentos-update',
  templateUrl: './controle-medicamentos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ControleMedicamentosUpdateComponent implements OnInit {
  isSaving = false;
  controleMedicamentos: IControleMedicamentos | null = null;

  usersSharedCollection: IUser[] = [];

  protected controleMedicamentosService = inject(ControleMedicamentosService);
  protected controleMedicamentosFormService = inject(ControleMedicamentosFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ControleMedicamentosFormGroup = this.controleMedicamentosFormService.createControleMedicamentosFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ controleMedicamentos }) => {
      this.controleMedicamentos = controleMedicamentos;
      if (controleMedicamentos) {
        this.updateForm(controleMedicamentos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const controleMedicamentos = this.controleMedicamentosFormService.getControleMedicamentos(this.editForm);
    if (controleMedicamentos.id !== null) {
      this.subscribeToSaveResponse(this.controleMedicamentosService.update(controleMedicamentos));
    } else {
      this.subscribeToSaveResponse(this.controleMedicamentosService.create(controleMedicamentos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IControleMedicamentos>>): void {
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

  protected updateForm(controleMedicamentos: IControleMedicamentos): void {
    this.controleMedicamentos = controleMedicamentos;
    this.controleMedicamentosFormService.resetForm(this.editForm, controleMedicamentos);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      controleMedicamentos.internalUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.controleMedicamentos?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
