import dayjs from 'dayjs/esm';

import { IControleMedicamentos, NewControleMedicamentos } from './controle-medicamentos.model';

export const sampleWithRequiredData: IControleMedicamentos = {
  id: 19475,
};

export const sampleWithPartialData: IControleMedicamentos = {
  id: 16738,
  nomeMedicamento: 'pinworm',
  dosagem: 'generally',
};

export const sampleWithFullData: IControleMedicamentos = {
  id: 28798,
  nomeMedicamento: 'viscose',
  dosagem: 'phooey unit',
  horarioIngestao: dayjs('2024-05-21T03:37'),
};

export const sampleWithNewData: NewControleMedicamentos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
