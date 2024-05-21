import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IConsumoAgua {
  id: number;
  dataConsumo?: dayjs.Dayjs | null;
  quantidadeMl?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewConsumoAgua = Omit<IConsumoAgua, 'id'> & { id: null };
