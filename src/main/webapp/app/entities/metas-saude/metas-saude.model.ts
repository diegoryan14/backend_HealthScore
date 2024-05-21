import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { TipoMeta } from 'app/entities/enumerations/tipo-meta.model';

export interface IMetasSaude {
  id: number;
  tipoMeta?: keyof typeof TipoMeta | null;
  valorMeta?: number | null;
  dataInicio?: dayjs.Dayjs | null;
  dataFim?: dayjs.Dayjs | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewMetasSaude = Omit<IMetasSaude, 'id'> & { id: null };
