import dayjs from 'dayjs/esm';

import { IConsumoAgua, NewConsumoAgua } from './consumo-agua.model';

export const sampleWithRequiredData: IConsumoAgua = {
  id: 25574,
};

export const sampleWithPartialData: IConsumoAgua = {
  id: 3180,
  dataConsumo: dayjs('2024-05-21T13:42'),
  quantidadeMl: 20929,
};

export const sampleWithFullData: IConsumoAgua = {
  id: 27380,
  dataConsumo: dayjs('2024-05-20T20:22'),
  quantidadeMl: 32513,
};

export const sampleWithNewData: NewConsumoAgua = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
