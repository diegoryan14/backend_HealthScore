import dayjs from 'dayjs/esm';

import { IMetasSaude, NewMetasSaude } from './metas-saude.model';

export const sampleWithRequiredData: IMetasSaude = {
  id: 2885,
};

export const sampleWithPartialData: IMetasSaude = {
  id: 27723,
  tipoMeta: 'OUTRO',
  valorMeta: 23545,
};

export const sampleWithFullData: IMetasSaude = {
  id: 23131,
  tipoMeta: 'OUTRO',
  valorMeta: 19653,
  dataInicio: dayjs('2024-05-21T11:30'),
  dataFim: dayjs('2024-05-20T21:56'),
};

export const sampleWithNewData: NewMetasSaude = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
