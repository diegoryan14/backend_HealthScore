import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDieta, NewDieta } from '../dieta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDieta for edit and NewDietaFormGroupInput for create.
 */
type DietaFormGroupInput = IDieta | PartialWithRequiredKeyOf<NewDieta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDieta | NewDieta> = Omit<T, 'dataHorarioRefeicao'> & {
  dataHorarioRefeicao?: string | null;
};

type DietaFormRawValue = FormValueOf<IDieta>;

type NewDietaFormRawValue = FormValueOf<NewDieta>;

type DietaFormDefaults = Pick<NewDieta, 'id' | 'dataHorarioRefeicao'>;

type DietaFormGroupContent = {
  id: FormControl<DietaFormRawValue['id'] | NewDieta['id']>;
  descricaoRefeicao: FormControl<DietaFormRawValue['descricaoRefeicao']>;
  dataHorarioRefeicao: FormControl<DietaFormRawValue['dataHorarioRefeicao']>;
  caloriasConsumidas: FormControl<DietaFormRawValue['caloriasConsumidas']>;
  internalUser: FormControl<DietaFormRawValue['internalUser']>;
};

export type DietaFormGroup = FormGroup<DietaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DietaFormService {
  createDietaFormGroup(dieta: DietaFormGroupInput = { id: null }): DietaFormGroup {
    const dietaRawValue = this.convertDietaToDietaRawValue({
      ...this.getFormDefaults(),
      ...dieta,
    });
    return new FormGroup<DietaFormGroupContent>({
      id: new FormControl(
        { value: dietaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descricaoRefeicao: new FormControl(dietaRawValue.descricaoRefeicao),
      dataHorarioRefeicao: new FormControl(dietaRawValue.dataHorarioRefeicao),
      caloriasConsumidas: new FormControl(dietaRawValue.caloriasConsumidas),
      internalUser: new FormControl(dietaRawValue.internalUser),
    });
  }

  getDieta(form: DietaFormGroup): IDieta | NewDieta {
    return this.convertDietaRawValueToDieta(form.getRawValue() as DietaFormRawValue | NewDietaFormRawValue);
  }

  resetForm(form: DietaFormGroup, dieta: DietaFormGroupInput): void {
    const dietaRawValue = this.convertDietaToDietaRawValue({ ...this.getFormDefaults(), ...dieta });
    form.reset(
      {
        ...dietaRawValue,
        id: { value: dietaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DietaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHorarioRefeicao: currentTime,
    };
  }

  private convertDietaRawValueToDieta(rawDieta: DietaFormRawValue | NewDietaFormRawValue): IDieta | NewDieta {
    return {
      ...rawDieta,
      dataHorarioRefeicao: dayjs(rawDieta.dataHorarioRefeicao, DATE_TIME_FORMAT),
    };
  }

  private convertDietaToDietaRawValue(
    dieta: IDieta | (Partial<NewDieta> & DietaFormDefaults),
  ): DietaFormRawValue | PartialWithRequiredKeyOf<NewDietaFormRawValue> {
    return {
      ...dieta,
      dataHorarioRefeicao: dieta.dataHorarioRefeicao ? dieta.dataHorarioRefeicao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
