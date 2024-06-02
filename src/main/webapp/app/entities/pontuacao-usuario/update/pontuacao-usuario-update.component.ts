import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IPontuacaoUsuario } from '../pontuacao-usuario.model';
import { PontuacaoUsuarioService } from '../service/pontuacao-usuario.service';
import { PontuacaoUsuarioFormService, PontuacaoUsuarioFormGroup } from './pontuacao-usuario-form.service';

@Component({
  standalone: true,
  selector: 'app-pontuacao-usuario-update',
  templateUrl: './pontuacao-usuario-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PontuacaoUsuarioUpdateComponent implements OnInit {
  isSaving = false;
  pontuacaoUsuario: IPontuacaoUsuario | null = null;

  usuariosSharedCollection: IUsuario[] = [];

  protected pontuacaoUsuarioService = inject(PontuacaoUsuarioService);
  protected pontuacaoUsuarioFormService = inject(PontuacaoUsuarioFormService);
  protected usuarioService = inject(UsuarioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PontuacaoUsuarioFormGroup = this.pontuacaoUsuarioFormService.createPontuacaoUsuarioFormGroup();

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pontuacaoUsuario }) => {
      this.pontuacaoUsuario = pontuacaoUsuario;
      if (pontuacaoUsuario) {
        this.updateForm(pontuacaoUsuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pontuacaoUsuario = this.pontuacaoUsuarioFormService.getPontuacaoUsuario(this.editForm);
    if (pontuacaoUsuario.id !== null) {
      this.subscribeToSaveResponse(this.pontuacaoUsuarioService.update(pontuacaoUsuario));
    } else {
      this.subscribeToSaveResponse(this.pontuacaoUsuarioService.create(pontuacaoUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPontuacaoUsuario>>): void {
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

  protected updateForm(pontuacaoUsuario: IPontuacaoUsuario): void {
    this.pontuacaoUsuario = pontuacaoUsuario;
    this.pontuacaoUsuarioFormService.resetForm(this.editForm, pontuacaoUsuario);

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(
      this.usuariosSharedCollection,
      pontuacaoUsuario.usuario,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, this.pontuacaoUsuario?.usuario),
        ),
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }
}
