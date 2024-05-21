import dayjs from 'dayjs/esm';

import { IQualidadeSono, NewQualidadeSono } from './qualidade-sono.model';

export const sampleWithRequiredData: IQualidadeSono = {
  id: 12632,
};

export const sampleWithPartialData: IQualidadeSono = {
  id: 13483,
  data: dayjs('2024-05-21T13:38'),
  horasSono: 5301,
};

export const sampleWithFullData: IQualidadeSono = {
  id: 18412,
  data: dayjs('2024-05-20T22:25'),
  horasSono: 9600,
};

export const sampleWithNewData: NewQualidadeSono = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
