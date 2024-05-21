import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Especializacao } from 'app/entities/enumerations/especializacao.model';
import { IEspecialista } from '../especialista.model';
import { EspecialistaService } from '../service/especialista.service';
import { EspecialistaFormService, EspecialistaFormGroup } from './especialista-form.service';

@Component({
  standalone: true,
  selector: 'app-especialista-update',
  templateUrl: './especialista-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EspecialistaUpdateComponent implements OnInit {
  isSaving = false;
  especialista: IEspecialista | null = null;
  especializacaoValues = Object.keys(Especializacao);

  protected especialistaService = inject(EspecialistaService);
  protected especialistaFormService = inject(EspecialistaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EspecialistaFormGroup = this.especialistaFormService.createEspecialistaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialista }) => {
      this.especialista = especialista;
      if (especialista) {
        this.updateForm(especialista);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialista = this.especialistaFormService.getEspecialista(this.editForm);
    if (especialista.id !== null) {
      this.subscribeToSaveResponse(this.especialistaService.update(especialista));
    } else {
      this.subscribeToSaveResponse(this.especialistaService.create(especialista));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialista>>): void {
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

  protected updateForm(especialista: IEspecialista): void {
    this.especialista = especialista;
    this.especialistaFormService.resetForm(this.editForm, especialista);
  }
}
