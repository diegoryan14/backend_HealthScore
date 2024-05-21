import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsultaEspecialista, NewConsultaEspecialista } from '../consulta-especialista.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsultaEspecialista for edit and NewConsultaEspecialistaFormGroupInput for create.
 */
type ConsultaEspecialistaFormGroupInput = IConsultaEspecialista | PartialWithRequiredKeyOf<NewConsultaEspecialista>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConsultaEspecialista | NewConsultaEspecialista> = Omit<T, 'dataHorarioConsulta'> & {
  dataHorarioConsulta?: string | null;
};

type ConsultaEspecialistaFormRawValue = FormValueOf<IConsultaEspecialista>;

type NewConsultaEspecialistaFormRawValue = FormValueOf<NewConsultaEspecialista>;

type ConsultaEspecialistaFormDefaults = Pick<NewConsultaEspecialista, 'id' | 'dataHorarioConsulta'>;

type ConsultaEspecialistaFormGroupContent = {
  id: FormControl<ConsultaEspecialistaFormRawValue['id'] | NewConsultaEspecialista['id']>;
  tipoEspecialista: FormControl<ConsultaEspecialistaFormRawValue['tipoEspecialista']>;
  dataHorarioConsulta: FormControl<ConsultaEspecialistaFormRawValue['dataHorarioConsulta']>;
  statusConsulta: FormControl<ConsultaEspecialistaFormRawValue['statusConsulta']>;
  linkConsulta: FormControl<ConsultaEspecialistaFormRawValue['linkConsulta']>;
  internalUser: FormControl<ConsultaEspecialistaFormRawValue['internalUser']>;
  especialista: FormControl<ConsultaEspecialistaFormRawValue['especialista']>;
};

export type ConsultaEspecialistaFormGroup = FormGroup<ConsultaEspecialistaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsultaEspecialistaFormService {
  createConsultaEspecialistaFormGroup(
    consultaEspecialista: ConsultaEspecialistaFormGroupInput = { id: null },
  ): ConsultaEspecialistaFormGroup {
    const consultaEspecialistaRawValue = this.convertConsultaEspecialistaToConsultaEspecialistaRawValue({
      ...this.getFormDefaults(),
      ...consultaEspecialista,
    });
    return new FormGroup<ConsultaEspecialistaFormGroupContent>({
      id: new FormControl(
        { value: consultaEspecialistaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipoEspecialista: new FormControl(consultaEspecialistaRawValue.tipoEspecialista),
      dataHorarioConsulta: new FormControl(consultaEspecialistaRawValue.dataHorarioConsulta),
      statusConsulta: new FormControl(consultaEspecialistaRawValue.statusConsulta),
      linkConsulta: new FormControl(consultaEspecialistaRawValue.linkConsulta),
      internalUser: new FormControl(consultaEspecialistaRawValue.internalUser),
      especialista: new FormControl(consultaEspecialistaRawValue.especialista),
    });
  }

  getConsultaEspecialista(form: ConsultaEspecialistaFormGroup): IConsultaEspecialista | NewConsultaEspecialista {
    return this.convertConsultaEspecialistaRawValueToConsultaEspecialista(
      form.getRawValue() as ConsultaEspecialistaFormRawValue | NewConsultaEspecialistaFormRawValue,
    );
  }

  resetForm(form: ConsultaEspecialistaFormGroup, consultaEspecialista: ConsultaEspecialistaFormGroupInput): void {
    const consultaEspecialistaRawValue = this.convertConsultaEspecialistaToConsultaEspecialistaRawValue({
      ...this.getFormDefaults(),
      ...consultaEspecialista,
    });
    form.reset(
      {
        ...consultaEspecialistaRawValue,
        id: { value: consultaEspecialistaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsultaEspecialistaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHorarioConsulta: currentTime,
    };
  }

  private convertConsultaEspecialistaRawValueToConsultaEspecialista(
    rawConsultaEspecialista: ConsultaEspecialistaFormRawValue | NewConsultaEspecialistaFormRawValue,
  ): IConsultaEspecialista | NewConsultaEspecialista {
    return {
      ...rawConsultaEspecialista,
      dataHorarioConsulta: dayjs(rawConsultaEspecialista.dataHorarioConsulta, DATE_TIME_FORMAT),
    };
  }

  private convertConsultaEspecialistaToConsultaEspecialistaRawValue(
    consultaEspecialista: IConsultaEspecialista | (Partial<NewConsultaEspecialista> & ConsultaEspecialistaFormDefaults),
  ): ConsultaEspecialistaFormRawValue | PartialWithRequiredKeyOf<NewConsultaEspecialistaFormRawValue> {
    return {
      ...consultaEspecialista,
      dataHorarioConsulta: consultaEspecialista.dataHorarioConsulta
        ? consultaEspecialista.dataHorarioConsulta.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
