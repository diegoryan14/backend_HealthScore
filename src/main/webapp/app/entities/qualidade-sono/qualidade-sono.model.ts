import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IQualidadeSono {
  id: number;
  data?: dayjs.Dayjs | null;
  horasSono?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewQualidadeSono = Omit<IQualidadeSono, 'id'> & { id: null };
