import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEspecialista, NewEspecialista } from '../especialista.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEspecialista for edit and NewEspecialistaFormGroupInput for create.
 */
type EspecialistaFormGroupInput = IEspecialista | PartialWithRequiredKeyOf<NewEspecialista>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEspecialista | NewEspecialista> = Omit<T, 'dataFormacao' | 'dataNascimento'> & {
  dataFormacao?: string | null;
  dataNascimento?: string | null;
};

type EspecialistaFormRawValue = FormValueOf<IEspecialista>;

type NewEspecialistaFormRawValue = FormValueOf<NewEspecialista>;

type EspecialistaFormDefaults = Pick<NewEspecialista, 'id' | 'dataFormacao' | 'dataNascimento'>;

type EspecialistaFormGroupContent = {
  id: FormControl<EspecialistaFormRawValue['id'] | NewEspecialista['id']>;
  nome: FormControl<EspecialistaFormRawValue['nome']>;
  cpf: FormControl<EspecialistaFormRawValue['cpf']>;
  especializacao: FormControl<EspecialistaFormRawValue['especializacao']>;
  dataFormacao: FormControl<EspecialistaFormRawValue['dataFormacao']>;
  telefone: FormControl<EspecialistaFormRawValue['telefone']>;
  email: FormControl<EspecialistaFormRawValue['email']>;
  dataNascimento: FormControl<EspecialistaFormRawValue['dataNascimento']>;
};

export type EspecialistaFormGroup = FormGroup<EspecialistaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EspecialistaFormService {
  createEspecialistaFormGroup(especialista: EspecialistaFormGroupInput = { id: null }): EspecialistaFormGroup {
    const especialistaRawValue = this.convertEspecialistaToEspecialistaRawValue({
      ...this.getFormDefaults(),
      ...especialista,
    });
    return new FormGroup<EspecialistaFormGroupContent>({
      id: new FormControl(
        { value: especialistaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(especialistaRawValue.nome),
      cpf: new FormControl(especialistaRawValue.cpf),
      especializacao: new FormControl(especialistaRawValue.especializacao),
      dataFormacao: new FormControl(especialistaRawValue.dataFormacao),
      telefone: new FormControl(especialistaRawValue.telefone),
      email: new FormControl(especialistaRawValue.email),
      dataNascimento: new FormControl(especialistaRawValue.dataNascimento),
    });
  }

  getEspecialista(form: EspecialistaFormGroup): IEspecialista | NewEspecialista {
    return this.convertEspecialistaRawValueToEspecialista(form.getRawValue() as EspecialistaFormRawValue | NewEspecialistaFormRawValue);
  }

  resetForm(form: EspecialistaFormGroup, especialista: EspecialistaFormGroupInput): void {
    const especialistaRawValue = this.convertEspecialistaToEspecialistaRawValue({ ...this.getFormDefaults(), ...especialista });
    form.reset(
      {
        ...especialistaRawValue,
        id: { value: especialistaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EspecialistaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataFormacao: currentTime,
      dataNascimento: currentTime,
    };
  }

  private convertEspecialistaRawValueToEspecialista(
    rawEspecialista: EspecialistaFormRawValue | NewEspecialistaFormRawValue,
  ): IEspecialista | NewEspecialista {
    return {
      ...rawEspecialista,
      dataFormacao: dayjs(rawEspecialista.dataFormacao, DATE_TIME_FORMAT),
      dataNascimento: dayjs(rawEspecialista.dataNascimento, DATE_TIME_FORMAT),
    };
  }

  private convertEspecialistaToEspecialistaRawValue(
    especialista: IEspecialista | (Partial<NewEspecialista> & EspecialistaFormDefaults),
  ): EspecialistaFormRawValue | PartialWithRequiredKeyOf<NewEspecialistaFormRawValue> {
    return {
      ...especialista,
      dataFormacao: especialista.dataFormacao ? especialista.dataFormacao.format(DATE_TIME_FORMAT) : undefined,
      dataNascimento: especialista.dataNascimento ? especialista.dataNascimento.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
