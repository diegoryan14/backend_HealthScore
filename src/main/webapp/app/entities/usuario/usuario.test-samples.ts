import dayjs from 'dayjs/esm';

import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 4032,
};

export const sampleWithPartialData: IUsuario = {
  id: 29650,
  dataRegistro: dayjs('2024-05-21T06:09'),
  telefone: 26905,
  email: 'Eduarda.Martins46@hotmail.com',
  metaConsumoAgua: 17823,
};

export const sampleWithFullData: IUsuario = {
  id: 5547,
  plano: 'PRATA',
  dataRegistro: dayjs('2024-05-21T03:43'),
  telefone: 27582,
  email: 'Sirineu_Silva37@bol.com.br',
  dataNascimento: dayjs('2024-05-20T18:40'),
  metaConsumoAgua: 31544,
  metaSono: 14548.22,
  metaCaloriasConsumidas: 31046.53,
  metaCaloriasQueimadas: 7060,
  pontosUser: 27402,
};

export const sampleWithNewData: NewUsuario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
