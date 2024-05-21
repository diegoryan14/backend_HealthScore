import dayjs from 'dayjs/esm';

import { IEspecialista, NewEspecialista } from './especialista.model';

export const sampleWithRequiredData: IEspecialista = {
  id: 13161,
};

export const sampleWithPartialData: IEspecialista = {
  id: 14483,
  cpf: 'supposing adventurously',
  dataFormacao: dayjs('2024-05-21T00:25'),
  telefone: 21033,
  email: 'Rafael30@live.com',
};

export const sampleWithFullData: IEspecialista = {
  id: 21642,
  nome: 'whether',
  cpf: 'times',
  especializacao: 'PERSONAL_TRAINER',
  dataFormacao: dayjs('2024-05-21T09:30'),
  telefone: 32452,
  email: 'Fabricia.Batista2@bol.com.br',
  dataNascimento: dayjs('2024-05-21T12:46'),
};

export const sampleWithNewData: NewEspecialista = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
