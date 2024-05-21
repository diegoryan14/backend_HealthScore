import dayjs from 'dayjs/esm';

import { IDieta, NewDieta } from './dieta.model';

export const sampleWithRequiredData: IDieta = {
  id: 25802,
};

export const sampleWithPartialData: IDieta = {
  id: 11980,
  descricaoRefeicao: 'inwardly per',
};

export const sampleWithFullData: IDieta = {
  id: 21389,
  descricaoRefeicao: 'wharf',
  dataHorarioRefeicao: dayjs('2024-05-21T10:03'),
  caloriasConsumidas: 2655,
};

export const sampleWithNewData: NewDieta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
