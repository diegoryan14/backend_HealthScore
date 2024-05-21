import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAnuncio, NewAnuncio } from '../anuncio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnuncio for edit and NewAnuncioFormGroupInput for create.
 */
type AnuncioFormGroupInput = IAnuncio | PartialWithRequiredKeyOf<NewAnuncio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAnuncio | NewAnuncio> = Omit<T, 'dataPublicacao' | 'dataInicio' | 'dataFim'> & {
  dataPublicacao?: string | null;
  dataInicio?: string | null;
  dataFim?: string | null;
};

type AnuncioFormRawValue = FormValueOf<IAnuncio>;

type NewAnuncioFormRawValue = FormValueOf<NewAnuncio>;

type AnuncioFormDefaults = Pick<NewAnuncio, 'id' | 'dataPublicacao' | 'dataInicio' | 'dataFim'>;

type AnuncioFormGroupContent = {
  id: FormControl<AnuncioFormRawValue['id'] | NewAnuncio['id']>;
  titulo: FormControl<AnuncioFormRawValue['titulo']>;
  descricao: FormControl<AnuncioFormRawValue['descricao']>;
  dataPublicacao: FormControl<AnuncioFormRawValue['dataPublicacao']>;
  dataInicio: FormControl<AnuncioFormRawValue['dataInicio']>;
  dataFim: FormControl<AnuncioFormRawValue['dataFim']>;
  preco: FormControl<AnuncioFormRawValue['preco']>;
};

export type AnuncioFormGroup = FormGroup<AnuncioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnuncioFormService {
  createAnuncioFormGroup(anuncio: AnuncioFormGroupInput = { id: null }): AnuncioFormGroup {
    const anuncioRawValue = this.convertAnuncioToAnuncioRawValue({
      ...this.getFormDefaults(),
      ...anuncio,
    });
    return new FormGroup<AnuncioFormGroupContent>({
      id: new FormControl(
        { value: anuncioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titulo: new FormControl(anuncioRawValue.titulo),
      descricao: new FormControl(anuncioRawValue.descricao),
      dataPublicacao: new FormControl(anuncioRawValue.dataPublicacao),
      dataInicio: new FormControl(anuncioRawValue.dataInicio),
      dataFim: new FormControl(anuncioRawValue.dataFim),
      preco: new FormControl(anuncioRawValue.preco),
    });
  }

  getAnuncio(form: AnuncioFormGroup): IAnuncio | NewAnuncio {
    return this.convertAnuncioRawValueToAnuncio(form.getRawValue() as AnuncioFormRawValue | NewAnuncioFormRawValue);
  }

  resetForm(form: AnuncioFormGroup, anuncio: AnuncioFormGroupInput): void {
    const anuncioRawValue = this.convertAnuncioToAnuncioRawValue({ ...this.getFormDefaults(), ...anuncio });
    form.reset(
      {
        ...anuncioRawValue,
        id: { value: anuncioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AnuncioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataPublicacao: currentTime,
      dataInicio: currentTime,
      dataFim: currentTime,
    };
  }

  private convertAnuncioRawValueToAnuncio(rawAnuncio: AnuncioFormRawValue | NewAnuncioFormRawValue): IAnuncio | NewAnuncio {
    return {
      ...rawAnuncio,
      dataPublicacao: dayjs(rawAnuncio.dataPublicacao, DATE_TIME_FORMAT),
      dataInicio: dayjs(rawAnuncio.dataInicio, DATE_TIME_FORMAT),
      dataFim: dayjs(rawAnuncio.dataFim, DATE_TIME_FORMAT),
    };
  }

  private convertAnuncioToAnuncioRawValue(
    anuncio: IAnuncio | (Partial<NewAnuncio> & AnuncioFormDefaults),
  ): AnuncioFormRawValue | PartialWithRequiredKeyOf<NewAnuncioFormRawValue> {
    return {
      ...anuncio,
      dataPublicacao: anuncio.dataPublicacao ? anuncio.dataPublicacao.format(DATE_TIME_FORMAT) : undefined,
      dataInicio: anuncio.dataInicio ? anuncio.dataInicio.format(DATE_TIME_FORMAT) : undefined,
      dataFim: anuncio.dataFim ? anuncio.dataFim.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
