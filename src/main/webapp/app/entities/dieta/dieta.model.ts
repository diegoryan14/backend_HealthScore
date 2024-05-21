import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IDieta {
  id: number;
  descricaoRefeicao?: string | null;
  dataHorarioRefeicao?: dayjs.Dayjs | null;
  caloriasConsumidas?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewDieta = Omit<IDieta, 'id'> & { id: null };
