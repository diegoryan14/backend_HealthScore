import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQualidadeSono, NewQualidadeSono } from '../qualidade-sono.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQualidadeSono for edit and NewQualidadeSonoFormGroupInput for create.
 */
type QualidadeSonoFormGroupInput = IQualidadeSono | PartialWithRequiredKeyOf<NewQualidadeSono>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQualidadeSono | NewQualidadeSono> = Omit<T, 'data'> & {
  data?: string | null;
};

type QualidadeSonoFormRawValue = FormValueOf<IQualidadeSono>;

type NewQualidadeSonoFormRawValue = FormValueOf<NewQualidadeSono>;

type QualidadeSonoFormDefaults = Pick<NewQualidadeSono, 'id' | 'data'>;

type QualidadeSonoFormGroupContent = {
  id: FormControl<QualidadeSonoFormRawValue['id'] | NewQualidadeSono['id']>;
  data: FormControl<QualidadeSonoFormRawValue['data']>;
  horasSono: FormControl<QualidadeSonoFormRawValue['horasSono']>;
  internalUser: FormControl<QualidadeSonoFormRawValue['internalUser']>;
};

export type QualidadeSonoFormGroup = FormGroup<QualidadeSonoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QualidadeSonoFormService {
  createQualidadeSonoFormGroup(qualidadeSono: QualidadeSonoFormGroupInput = { id: null }): QualidadeSonoFormGroup {
    const qualidadeSonoRawValue = this.convertQualidadeSonoToQualidadeSonoRawValue({
      ...this.getFormDefaults(),
      ...qualidadeSono,
    });
    return new FormGroup<QualidadeSonoFormGroupContent>({
      id: new FormControl(
        { value: qualidadeSonoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      data: new FormControl(qualidadeSonoRawValue.data),
      horasSono: new FormControl(qualidadeSonoRawValue.horasSono),
      internalUser: new FormControl(qualidadeSonoRawValue.internalUser),
    });
  }

  getQualidadeSono(form: QualidadeSonoFormGroup): IQualidadeSono | NewQualidadeSono {
    return this.convertQualidadeSonoRawValueToQualidadeSono(form.getRawValue() as QualidadeSonoFormRawValue | NewQualidadeSonoFormRawValue);
  }

  resetForm(form: QualidadeSonoFormGroup, qualidadeSono: QualidadeSonoFormGroupInput): void {
    const qualidadeSonoRawValue = this.convertQualidadeSonoToQualidadeSonoRawValue({ ...this.getFormDefaults(), ...qualidadeSono });
    form.reset(
      {
        ...qualidadeSonoRawValue,
        id: { value: qualidadeSonoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QualidadeSonoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      data: currentTime,
    };
  }

  private convertQualidadeSonoRawValueToQualidadeSono(
    rawQualidadeSono: QualidadeSonoFormRawValue | NewQualidadeSonoFormRawValue,
  ): IQualidadeSono | NewQualidadeSono {
    return {
      ...rawQualidadeSono,
      data: dayjs(rawQualidadeSono.data, DATE_TIME_FORMAT),
    };
  }

  private convertQualidadeSonoToQualidadeSonoRawValue(
    qualidadeSono: IQualidadeSono | (Partial<NewQualidadeSono> & QualidadeSonoFormDefaults),
  ): QualidadeSonoFormRawValue | PartialWithRequiredKeyOf<NewQualidadeSonoFormRawValue> {
    return {
      ...qualidadeSono,
      data: qualidadeSono.data ? qualidadeSono.data.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
