import dayjs from 'dayjs/esm';

import { IPontuacaoUsuario, NewPontuacaoUsuario } from './pontuacao-usuario.model';

export const sampleWithRequiredData: IPontuacaoUsuario = {
  id: 20327,
};

export const sampleWithPartialData: IPontuacaoUsuario = {
  id: 22107,
  dataAlteracao: dayjs('2024-06-02T00:40'),
  valorAlteracao: 6009,
  motivo: 'before faithfully',
};

export const sampleWithFullData: IPontuacaoUsuario = {
  id: 20654,
  dataAlteracao: dayjs('2024-06-01T21:32'),
  valorAlteracao: 21765,
  motivo: 'unveil anti',
};

export const sampleWithNewData: NewPontuacaoUsuario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
