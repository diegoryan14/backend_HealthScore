import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUsuario, NewUsuario } from '../usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuario for edit and NewUsuarioFormGroupInput for create.
 */
type UsuarioFormGroupInput = IUsuario | PartialWithRequiredKeyOf<NewUsuario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUsuario | NewUsuario> = Omit<T, 'dataRegistro' | 'dataNascimento'> & {
  dataRegistro?: string | null;
  dataNascimento?: string | null;
};

type UsuarioFormRawValue = FormValueOf<IUsuario>;

type NewUsuarioFormRawValue = FormValueOf<NewUsuario>;

type UsuarioFormDefaults = Pick<NewUsuario, 'id' | 'dataRegistro' | 'dataNascimento'>;

type UsuarioFormGroupContent = {
  id: FormControl<UsuarioFormRawValue['id'] | NewUsuario['id']>;
  plano: FormControl<UsuarioFormRawValue['plano']>;
  dataRegistro: FormControl<UsuarioFormRawValue['dataRegistro']>;
  telefone: FormControl<UsuarioFormRawValue['telefone']>;
  email: FormControl<UsuarioFormRawValue['email']>;
  dataNascimento: FormControl<UsuarioFormRawValue['dataNascimento']>;
  metaConsumoAgua: FormControl<UsuarioFormRawValue['metaConsumoAgua']>;
  metaSono: FormControl<UsuarioFormRawValue['metaSono']>;
  metaCaloriasConsumidas: FormControl<UsuarioFormRawValue['metaCaloriasConsumidas']>;
  metaCaloriasQueimadas: FormControl<UsuarioFormRawValue['metaCaloriasQueimadas']>;
  pontosUser: FormControl<UsuarioFormRawValue['pontosUser']>;
  genero: FormControl<UsuarioFormRawValue['genero']>;
  internalUser: FormControl<UsuarioFormRawValue['internalUser']>;
};

export type UsuarioFormGroup = FormGroup<UsuarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuarioFormService {
  createUsuarioFormGroup(usuario: UsuarioFormGroupInput = { id: null }): UsuarioFormGroup {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({
      ...this.getFormDefaults(),
      ...usuario,
    });
    return new FormGroup<UsuarioFormGroupContent>({
      id: new FormControl(
        { value: usuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      plano: new FormControl(usuarioRawValue.plano),
      dataRegistro: new FormControl(usuarioRawValue.dataRegistro),
      telefone: new FormControl(usuarioRawValue.telefone),
      email: new FormControl(usuarioRawValue.email),
      dataNascimento: new FormControl(usuarioRawValue.dataNascimento),
      metaConsumoAgua: new FormControl(usuarioRawValue.metaConsumoAgua),
      metaSono: new FormControl(usuarioRawValue.metaSono),
      metaCaloriasConsumidas: new FormControl(usuarioRawValue.metaCaloriasConsumidas),
      metaCaloriasQueimadas: new FormControl(usuarioRawValue.metaCaloriasQueimadas),
      pontosUser: new FormControl(usuarioRawValue.pontosUser),
      genero: new FormControl(usuarioRawValue.genero),
      internalUser: new FormControl(usuarioRawValue.internalUser),
    });
  }

  getUsuario(form: UsuarioFormGroup): IUsuario | NewUsuario {
    return this.convertUsuarioRawValueToUsuario(form.getRawValue() as UsuarioFormRawValue | NewUsuarioFormRawValue);
  }

  resetForm(form: UsuarioFormGroup, usuario: UsuarioFormGroupInput): void {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({ ...this.getFormDefaults(), ...usuario });
    form.reset(
      {
        ...usuarioRawValue,
        id: { value: usuarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UsuarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataRegistro: currentTime,
      dataNascimento: currentTime,
    };
  }

  private convertUsuarioRawValueToUsuario(rawUsuario: UsuarioFormRawValue | NewUsuarioFormRawValue): IUsuario | NewUsuario {
    return {
      ...rawUsuario,
      dataRegistro: dayjs(rawUsuario.dataRegistro, DATE_TIME_FORMAT),
      dataNascimento: dayjs(rawUsuario.dataNascimento, DATE_TIME_FORMAT),
    };
  }

  private convertUsuarioToUsuarioRawValue(
    usuario: IUsuario | (Partial<NewUsuario> & UsuarioFormDefaults),
  ): UsuarioFormRawValue | PartialWithRequiredKeyOf<NewUsuarioFormRawValue> {
    return {
      ...usuario,
      dataRegistro: usuario.dataRegistro ? usuario.dataRegistro.format(DATE_TIME_FORMAT) : undefined,
      dataNascimento: usuario.dataNascimento ? usuario.dataNascimento.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
