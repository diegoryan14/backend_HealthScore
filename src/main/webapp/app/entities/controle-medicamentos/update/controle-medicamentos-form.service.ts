import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IControleMedicamentos, NewControleMedicamentos } from '../controle-medicamentos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IControleMedicamentos for edit and NewControleMedicamentosFormGroupInput for create.
 */
type ControleMedicamentosFormGroupInput = IControleMedicamentos | PartialWithRequiredKeyOf<NewControleMedicamentos>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IControleMedicamentos | NewControleMedicamentos> = Omit<T, 'horarioIngestao'> & {
  horarioIngestao?: string | null;
};

type ControleMedicamentosFormRawValue = FormValueOf<IControleMedicamentos>;

type NewControleMedicamentosFormRawValue = FormValueOf<NewControleMedicamentos>;

type ControleMedicamentosFormDefaults = Pick<NewControleMedicamentos, 'id' | 'horarioIngestao'>;

type ControleMedicamentosFormGroupContent = {
  id: FormControl<ControleMedicamentosFormRawValue['id'] | NewControleMedicamentos['id']>;
  nomeMedicamento: FormControl<ControleMedicamentosFormRawValue['nomeMedicamento']>;
  dosagem: FormControl<ControleMedicamentosFormRawValue['dosagem']>;
  horarioIngestao: FormControl<ControleMedicamentosFormRawValue['horarioIngestao']>;
  internalUser: FormControl<ControleMedicamentosFormRawValue['internalUser']>;
};

export type ControleMedicamentosFormGroup = FormGroup<ControleMedicamentosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ControleMedicamentosFormService {
  createControleMedicamentosFormGroup(
    controleMedicamentos: ControleMedicamentosFormGroupInput = { id: null },
  ): ControleMedicamentosFormGroup {
    const controleMedicamentosRawValue = this.convertControleMedicamentosToControleMedicamentosRawValue({
      ...this.getFormDefaults(),
      ...controleMedicamentos,
    });
    return new FormGroup<ControleMedicamentosFormGroupContent>({
      id: new FormControl(
        { value: controleMedicamentosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomeMedicamento: new FormControl(controleMedicamentosRawValue.nomeMedicamento),
      dosagem: new FormControl(controleMedicamentosRawValue.dosagem),
      horarioIngestao: new FormControl(controleMedicamentosRawValue.horarioIngestao),
      internalUser: new FormControl(controleMedicamentosRawValue.internalUser),
    });
  }

  getControleMedicamentos(form: ControleMedicamentosFormGroup): IControleMedicamentos | NewControleMedicamentos {
    return this.convertControleMedicamentosRawValueToControleMedicamentos(
      form.getRawValue() as ControleMedicamentosFormRawValue | NewControleMedicamentosFormRawValue,
    );
  }

  resetForm(form: ControleMedicamentosFormGroup, controleMedicamentos: ControleMedicamentosFormGroupInput): void {
    const controleMedicamentosRawValue = this.convertControleMedicamentosToControleMedicamentosRawValue({
      ...this.getFormDefaults(),
      ...controleMedicamentos,
    });
    form.reset(
      {
        ...controleMedicamentosRawValue,
        id: { value: controleMedicamentosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ControleMedicamentosFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      horarioIngestao: currentTime,
    };
  }

  private convertControleMedicamentosRawValueToControleMedicamentos(
    rawControleMedicamentos: ControleMedicamentosFormRawValue | NewControleMedicamentosFormRawValue,
  ): IControleMedicamentos | NewControleMedicamentos {
    return {
      ...rawControleMedicamentos,
      horarioIngestao: dayjs(rawControleMedicamentos.horarioIngestao, DATE_TIME_FORMAT),
    };
  }

  private convertControleMedicamentosToControleMedicamentosRawValue(
    controleMedicamentos: IControleMedicamentos | (Partial<NewControleMedicamentos> & ControleMedicamentosFormDefaults),
  ): ControleMedicamentosFormRawValue | PartialWithRequiredKeyOf<NewControleMedicamentosFormRawValue> {
    return {
      ...controleMedicamentos,
      horarioIngestao: controleMedicamentos.horarioIngestao ? controleMedicamentos.horarioIngestao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
