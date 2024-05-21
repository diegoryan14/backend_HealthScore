import dayjs from 'dayjs/esm';

import { IConsultaEspecialista, NewConsultaEspecialista } from './consulta-especialista.model';

export const sampleWithRequiredData: IConsultaEspecialista = {
  id: 9550,
};

export const sampleWithPartialData: IConsultaEspecialista = {
  id: 5117,
  linkConsulta: 'knuckle',
};

export const sampleWithFullData: IConsultaEspecialista = {
  id: 11785,
  tipoEspecialista: 'NUTRICIONISTA',
  dataHorarioConsulta: dayjs('2024-05-21T14:24'),
  statusConsulta: 'ADIADA',
  linkConsulta: 'yum',
};

export const sampleWithNewData: NewConsultaEspecialista = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
