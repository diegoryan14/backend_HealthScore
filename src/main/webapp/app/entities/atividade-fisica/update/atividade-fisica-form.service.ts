import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAtividadeFisica, NewAtividadeFisica } from '../atividade-fisica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAtividadeFisica for edit and NewAtividadeFisicaFormGroupInput for create.
 */
type AtividadeFisicaFormGroupInput = IAtividadeFisica | PartialWithRequiredKeyOf<NewAtividadeFisica>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAtividadeFisica | NewAtividadeFisica> = Omit<T, 'dataHorario'> & {
  dataHorario?: string | null;
};

type AtividadeFisicaFormRawValue = FormValueOf<IAtividadeFisica>;

type NewAtividadeFisicaFormRawValue = FormValueOf<NewAtividadeFisica>;

type AtividadeFisicaFormDefaults = Pick<NewAtividadeFisica, 'id' | 'dataHorario'>;

type AtividadeFisicaFormGroupContent = {
  id: FormControl<AtividadeFisicaFormRawValue['id'] | NewAtividadeFisica['id']>;
  tipoAtividade: FormControl<AtividadeFisicaFormRawValue['tipoAtividade']>;
  dataHorario: FormControl<AtividadeFisicaFormRawValue['dataHorario']>;
  duracao: FormControl<AtividadeFisicaFormRawValue['duracao']>;
  passosCalorias: FormControl<AtividadeFisicaFormRawValue['passosCalorias']>;
  internalUser: FormControl<AtividadeFisicaFormRawValue['internalUser']>;
};

export type AtividadeFisicaFormGroup = FormGroup<AtividadeFisicaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AtividadeFisicaFormService {
  createAtividadeFisicaFormGroup(atividadeFisica: AtividadeFisicaFormGroupInput = { id: null }): AtividadeFisicaFormGroup {
    const atividadeFisicaRawValue = this.convertAtividadeFisicaToAtividadeFisicaRawValue({
      ...this.getFormDefaults(),
      ...atividadeFisica,
    });
    return new FormGroup<AtividadeFisicaFormGroupContent>({
      id: new FormControl(
        { value: atividadeFisicaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipoAtividade: new FormControl(atividadeFisicaRawValue.tipoAtividade),
      dataHorario: new FormControl(atividadeFisicaRawValue.dataHorario),
      duracao: new FormControl(atividadeFisicaRawValue.duracao),
      passosCalorias: new FormControl(atividadeFisicaRawValue.passosCalorias),
      internalUser: new FormControl(atividadeFisicaRawValue.internalUser),
    });
  }

  getAtividadeFisica(form: AtividadeFisicaFormGroup): IAtividadeFisica | NewAtividadeFisica {
    return this.convertAtividadeFisicaRawValueToAtividadeFisica(
      form.getRawValue() as AtividadeFisicaFormRawValue | NewAtividadeFisicaFormRawValue,
    );
  }

  resetForm(form: AtividadeFisicaFormGroup, atividadeFisica: AtividadeFisicaFormGroupInput): void {
    const atividadeFisicaRawValue = this.convertAtividadeFisicaToAtividadeFisicaRawValue({ ...this.getFormDefaults(), ...atividadeFisica });
    form.reset(
      {
        ...atividadeFisicaRawValue,
        id: { value: atividadeFisicaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AtividadeFisicaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHorario: currentTime,
    };
  }

  private convertAtividadeFisicaRawValueToAtividadeFisica(
    rawAtividadeFisica: AtividadeFisicaFormRawValue | NewAtividadeFisicaFormRawValue,
  ): IAtividadeFisica | NewAtividadeFisica {
    return {
      ...rawAtividadeFisica,
      dataHorario: dayjs(rawAtividadeFisica.dataHorario, DATE_TIME_FORMAT),
    };
  }

  private convertAtividadeFisicaToAtividadeFisicaRawValue(
    atividadeFisica: IAtividadeFisica | (Partial<NewAtividadeFisica> & AtividadeFisicaFormDefaults),
  ): AtividadeFisicaFormRawValue | PartialWithRequiredKeyOf<NewAtividadeFisicaFormRawValue> {
    return {
      ...atividadeFisica,
      dataHorario: atividadeFisica.dataHorario ? atividadeFisica.dataHorario.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
