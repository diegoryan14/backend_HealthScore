import dayjs from 'dayjs/esm';

import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 19737,
};

export const sampleWithPartialData: IUsuario = {
  id: 26905,
  plano: 'GRATUITO',
  dataRegistro: dayjs('2024-05-21T12:16'),
  telefone: 31203,
  dataNascimento: dayjs('2024-05-20T20:52'),
  genero: 'MASCULINO',
};

export const sampleWithFullData: IUsuario = {
  id: 520,
  plano: 'GRATUITO',
  dataRegistro: dayjs('2024-05-21T04:23'),
  telefone: 5547,
  email: 'Lucas2@live.com',
  dataNascimento: dayjs('2024-05-21T15:43'),
  metaConsumoAgua: 28495,
  metaSono: 12373.06,
  metaCaloriasConsumidas: 31093,
  metaCaloriasQueimadas: 31543.46,
  pontosUser: 14548,
  genero: 'NAO_IDENTIFICADO',
};

export const sampleWithNewData: NewUsuario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
