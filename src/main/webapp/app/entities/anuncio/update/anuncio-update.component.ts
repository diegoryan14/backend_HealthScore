import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnuncio } from '../anuncio.model';
import { AnuncioService } from '../service/anuncio.service';
import { AnuncioFormService, AnuncioFormGroup } from './anuncio-form.service';

@Component({
  standalone: true,
  selector: 'app-anuncio-update',
  templateUrl: './anuncio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnuncioUpdateComponent implements OnInit {
  isSaving = false;
  anuncio: IAnuncio | null = null;

  protected anuncioService = inject(AnuncioService);
  protected anuncioFormService = inject(AnuncioFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AnuncioFormGroup = this.anuncioFormService.createAnuncioFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anuncio }) => {
      this.anuncio = anuncio;
      if (anuncio) {
        this.updateForm(anuncio);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const anuncio = this.anuncioFormService.getAnuncio(this.editForm);
    if (anuncio.id !== null) {
      this.subscribeToSaveResponse(this.anuncioService.update(anuncio));
    } else {
      this.subscribeToSaveResponse(this.anuncioService.create(anuncio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnuncio>>): void {
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

  protected updateForm(anuncio: IAnuncio): void {
    this.anuncio = anuncio;
    this.anuncioFormService.resetForm(this.editForm, anuncio);
  }
}
