import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsumoAgua, NewConsumoAgua } from '../consumo-agua.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsumoAgua for edit and NewConsumoAguaFormGroupInput for create.
 */
type ConsumoAguaFormGroupInput = IConsumoAgua | PartialWithRequiredKeyOf<NewConsumoAgua>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConsumoAgua | NewConsumoAgua> = Omit<T, 'dataConsumo'> & {
  dataConsumo?: string | null;
};

type ConsumoAguaFormRawValue = FormValueOf<IConsumoAgua>;

type NewConsumoAguaFormRawValue = FormValueOf<NewConsumoAgua>;

type ConsumoAguaFormDefaults = Pick<NewConsumoAgua, 'id' | 'dataConsumo'>;

type ConsumoAguaFormGroupContent = {
  id: FormControl<ConsumoAguaFormRawValue['id'] | NewConsumoAgua['id']>;
  dataConsumo: FormControl<ConsumoAguaFormRawValue['dataConsumo']>;
  quantidadeMl: FormControl<ConsumoAguaFormRawValue['quantidadeMl']>;
  internalUser: FormControl<ConsumoAguaFormRawValue['internalUser']>;
};

export type ConsumoAguaFormGroup = FormGroup<ConsumoAguaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsumoAguaFormService {
  createConsumoAguaFormGroup(consumoAgua: ConsumoAguaFormGroupInput = { id: null }): ConsumoAguaFormGroup {
    const consumoAguaRawValue = this.convertConsumoAguaToConsumoAguaRawValue({
      ...this.getFormDefaults(),
      ...consumoAgua,
    });
    return new FormGroup<ConsumoAguaFormGroupContent>({
      id: new FormControl(
        { value: consumoAguaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataConsumo: new FormControl(consumoAguaRawValue.dataConsumo),
      quantidadeMl: new FormControl(consumoAguaRawValue.quantidadeMl),
      internalUser: new FormControl(consumoAguaRawValue.internalUser),
    });
  }

  getConsumoAgua(form: ConsumoAguaFormGroup): IConsumoAgua | NewConsumoAgua {
    return this.convertConsumoAguaRawValueToConsumoAgua(form.getRawValue() as ConsumoAguaFormRawValue | NewConsumoAguaFormRawValue);
  }

  resetForm(form: ConsumoAguaFormGroup, consumoAgua: ConsumoAguaFormGroupInput): void {
    const consumoAguaRawValue = this.convertConsumoAguaToConsumoAguaRawValue({ ...this.getFormDefaults(), ...consumoAgua });
    form.reset(
      {
        ...consumoAguaRawValue,
        id: { value: consumoAguaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsumoAguaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataConsumo: currentTime,
    };
  }

  private convertConsumoAguaRawValueToConsumoAgua(
    rawConsumoAgua: ConsumoAguaFormRawValue | NewConsumoAguaFormRawValue,
  ): IConsumoAgua | NewConsumoAgua {
    return {
      ...rawConsumoAgua,
      dataConsumo: dayjs(rawConsumoAgua.dataConsumo, DATE_TIME_FORMAT),
    };
  }

  private convertConsumoAguaToConsumoAguaRawValue(
    consumoAgua: IConsumoAgua | (Partial<NewConsumoAgua> & ConsumoAguaFormDefaults),
  ): ConsumoAguaFormRawValue | PartialWithRequiredKeyOf<NewConsumoAguaFormRawValue> {
    return {
      ...consumoAgua,
      dataConsumo: consumoAgua.dataConsumo ? consumoAgua.dataConsumo.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
