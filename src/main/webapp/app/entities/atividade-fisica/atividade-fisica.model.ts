import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { TipoAtividade } from 'app/entities/enumerations/tipo-atividade.model';

export interface IAtividadeFisica {
  id: number;
  tipoAtividade?: keyof typeof TipoAtividade | null;
  dataHorario?: dayjs.Dayjs | null;
  duracao?: number | null;
  passosCalorias?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewAtividadeFisica = Omit<IAtividadeFisica, 'id'> & { id: null };
