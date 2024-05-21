import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IControleMedicamentos {
  id: number;
  nomeMedicamento?: string | null;
  dosagem?: string | null;
  horarioIngestao?: dayjs.Dayjs | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewControleMedicamentos = Omit<IControleMedicamentos, 'id'> & { id: null };
