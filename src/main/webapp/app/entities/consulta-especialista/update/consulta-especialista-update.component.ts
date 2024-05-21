import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IEspecialista } from 'app/entities/especialista/especialista.model';
import { EspecialistaService } from 'app/entities/especialista/service/especialista.service';
import { TipoEspecialista } from 'app/entities/enumerations/tipo-especialista.model';
import { StatusConsulta } from 'app/entities/enumerations/status-consulta.model';
import { ConsultaEspecialistaService } from '../service/consulta-especialista.service';
import { IConsultaEspecialista } from '../consulta-especialista.model';
import { ConsultaEspecialistaFormService, ConsultaEspecialistaFormGroup } from './consulta-especialista-form.service';

@Component({
  standalone: true,
  selector: 'app-consulta-especialista-update',
  templateUrl: './consulta-especialista-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsultaEspecialistaUpdateComponent implements OnInit {
  isSaving = false;
  consultaEspecialista: IConsultaEspecialista | null = null;
  tipoEspecialistaValues = Object.keys(TipoEspecialista);
  statusConsultaValues = Object.keys(StatusConsulta);

  usersSharedCollection: IUser[] = [];
  especialistasSharedCollection: IEspecialista[] = [];

  protected consultaEspecialistaService = inject(ConsultaEspecialistaService);
  protected consultaEspecialistaFormService = inject(ConsultaEspecialistaFormService);
  protected userService = inject(UserService);
  protected especialistaService = inject(EspecialistaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConsultaEspecialistaFormGroup = this.consultaEspecialistaFormService.createConsultaEspecialistaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEspecialista = (o1: IEspecialista | null, o2: IEspecialista | null): boolean =>
    this.especialistaService.compareEspecialista(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultaEspecialista }) => {
      this.consultaEspecialista = consultaEspecialista;
      if (consultaEspecialista) {
        this.updateForm(consultaEspecialista);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultaEspecialista = this.consultaEspecialistaFormService.getConsultaEspecialista(this.editForm);
    if (consultaEspecialista.id !== null) {
      this.subscribeToSaveResponse(this.consultaEspecialistaService.update(consultaEspecialista));
    } else {
      this.subscribeToSaveResponse(this.consultaEspecialistaService.create(consultaEspecialista));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultaEspecialista>>): void {
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

  protected updateForm(consultaEspecialista: IConsultaEspecialista): void {
    this.consultaEspecialista = consultaEspecialista;
    this.consultaEspecialistaFormService.resetForm(this.editForm, consultaEspecialista);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      consultaEspecialista.internalUser,
    );
    this.especialistasSharedCollection = this.especialistaService.addEspecialistaToCollectionIfMissing<IEspecialista>(
      this.especialistasSharedCollection,
      consultaEspecialista.especialista,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.consultaEspecialista?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.especialistaService
      .query()
      .pipe(map((res: HttpResponse<IEspecialista[]>) => res.body ?? []))
      .pipe(
        map((especialistas: IEspecialista[]) =>
          this.especialistaService.addEspecialistaToCollectionIfMissing<IEspecialista>(
            especialistas,
            this.consultaEspecialista?.especialista,
          ),
        ),
      )
      .subscribe((especialistas: IEspecialista[]) => (this.especialistasSharedCollection = especialistas));
  }
}
