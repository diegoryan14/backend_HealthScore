import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPontuacaoUsuario, NewPontuacaoUsuario } from '../pontuacao-usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPontuacaoUsuario for edit and NewPontuacaoUsuarioFormGroupInput for create.
 */
type PontuacaoUsuarioFormGroupInput = IPontuacaoUsuario | PartialWithRequiredKeyOf<NewPontuacaoUsuario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPontuacaoUsuario | NewPontuacaoUsuario> = Omit<T, 'dataAlteracao'> & {
  dataAlteracao?: string | null;
};

type PontuacaoUsuarioFormRawValue = FormValueOf<IPontuacaoUsuario>;

type NewPontuacaoUsuarioFormRawValue = FormValueOf<NewPontuacaoUsuario>;

type PontuacaoUsuarioFormDefaults = Pick<NewPontuacaoUsuario, 'id' | 'dataAlteracao'>;

type PontuacaoUsuarioFormGroupContent = {
  id: FormControl<PontuacaoUsuarioFormRawValue['id'] | NewPontuacaoUsuario['id']>;
  dataAlteracao: FormControl<PontuacaoUsuarioFormRawValue['dataAlteracao']>;
  valorAlteracao: FormControl<PontuacaoUsuarioFormRawValue['valorAlteracao']>;
  motivo: FormControl<PontuacaoUsuarioFormRawValue['motivo']>;
  usuario: FormControl<PontuacaoUsuarioFormRawValue['usuario']>;
};

export type PontuacaoUsuarioFormGroup = FormGroup<PontuacaoUsuarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PontuacaoUsuarioFormService {
  createPontuacaoUsuarioFormGroup(pontuacaoUsuario: PontuacaoUsuarioFormGroupInput = { id: null }): PontuacaoUsuarioFormGroup {
    const pontuacaoUsuarioRawValue = this.convertPontuacaoUsuarioToPontuacaoUsuarioRawValue({
      ...this.getFormDefaults(),
      ...pontuacaoUsuario,
    });
    return new FormGroup<PontuacaoUsuarioFormGroupContent>({
      id: new FormControl(
        { value: pontuacaoUsuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataAlteracao: new FormControl(pontuacaoUsuarioRawValue.dataAlteracao),
      valorAlteracao: new FormControl(pontuacaoUsuarioRawValue.valorAlteracao),
      motivo: new FormControl(pontuacaoUsuarioRawValue.motivo),
      usuario: new FormControl(pontuacaoUsuarioRawValue.usuario),
    });
  }

  getPontuacaoUsuario(form: PontuacaoUsuarioFormGroup): IPontuacaoUsuario | NewPontuacaoUsuario {
    return this.convertPontuacaoUsuarioRawValueToPontuacaoUsuario(
      form.getRawValue() as PontuacaoUsuarioFormRawValue | NewPontuacaoUsuarioFormRawValue,
    );
  }

  resetForm(form: PontuacaoUsuarioFormGroup, pontuacaoUsuario: PontuacaoUsuarioFormGroupInput): void {
    const pontuacaoUsuarioRawValue = this.convertPontuacaoUsuarioToPontuacaoUsuarioRawValue({
      ...this.getFormDefaults(),
      ...pontuacaoUsuario,
    });
    form.reset(
      {
        ...pontuacaoUsuarioRawValue,
        id: { value: pontuacaoUsuarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PontuacaoUsuarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataAlteracao: currentTime,
    };
  }

  private convertPontuacaoUsuarioRawValueToPontuacaoUsuario(
    rawPontuacaoUsuario: PontuacaoUsuarioFormRawValue | NewPontuacaoUsuarioFormRawValue,
  ): IPontuacaoUsuario | NewPontuacaoUsuario {
    return {
      ...rawPontuacaoUsuario,
      dataAlteracao: dayjs(rawPontuacaoUsuario.dataAlteracao, DATE_TIME_FORMAT),
    };
  }

  private convertPontuacaoUsuarioToPontuacaoUsuarioRawValue(
    pontuacaoUsuario: IPontuacaoUsuario | (Partial<NewPontuacaoUsuario> & PontuacaoUsuarioFormDefaults),
  ): PontuacaoUsuarioFormRawValue | PartialWithRequiredKeyOf<NewPontuacaoUsuarioFormRawValue> {
    return {
      ...pontuacaoUsuario,
      dataAlteracao: pontuacaoUsuario.dataAlteracao ? pontuacaoUsuario.dataAlteracao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
