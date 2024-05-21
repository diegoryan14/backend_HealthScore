import dayjs from 'dayjs/esm';
import { Especializacao } from 'app/entities/enumerations/especializacao.model';

export interface IEspecialista {
  id: number;
  nome?: string | null;
  cpf?: string | null;
  especializacao?: keyof typeof Especializacao | null;
  dataFormacao?: dayjs.Dayjs | null;
  telefone?: number | null;
  email?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
}

export type NewEspecialista = Omit<IEspecialista, 'id'> & { id: null };
