import dayjs from 'dayjs/esm';

import { IAnuncio, NewAnuncio } from './anuncio.model';

export const sampleWithRequiredData: IAnuncio = {
  id: 19429,
};

export const sampleWithPartialData: IAnuncio = {
  id: 5372,
  dataPublicacao: dayjs('2024-05-21T00:18'),
};

export const sampleWithFullData: IAnuncio = {
  id: 27936,
  titulo: 'automaton',
  descricao: 'foolhardy how',
  dataPublicacao: dayjs('2024-05-20T18:16'),
  dataInicio: dayjs('2024-05-21T07:17'),
  dataFim: dayjs('2024-05-20T21:53'),
  preco: 20134.1,
};

export const sampleWithNewData: NewAnuncio = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
