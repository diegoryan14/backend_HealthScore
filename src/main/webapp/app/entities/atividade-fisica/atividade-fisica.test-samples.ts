import dayjs from 'dayjs/esm';

import { IAtividadeFisica, NewAtividadeFisica } from './atividade-fisica.model';

export const sampleWithRequiredData: IAtividadeFisica = {
  id: 16238,
};

export const sampleWithPartialData: IAtividadeFisica = {
  id: 2334,
  dataHorario: dayjs('2024-05-21T08:06'),
  duracao: 25416,
};

export const sampleWithFullData: IAtividadeFisica = {
  id: 10930,
  tipoAtividade: 'CAMINHADA',
  dataHorario: dayjs('2024-05-20T19:47'),
  duracao: 23748,
  passosCalorias: 1032,
};

export const sampleWithNewData: NewAtividadeFisica = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
