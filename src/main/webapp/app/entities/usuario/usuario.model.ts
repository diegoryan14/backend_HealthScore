import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { TipoPlano } from 'app/entities/enumerations/tipo-plano.model';

export interface IUsuario {
  id: number;
  plano?: keyof typeof TipoPlano | null;
  dataRegistro?: dayjs.Dayjs | null;
  telefone?: number | null;
  email?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  metaConsumoAgua?: number | null;
  metaSono?: number | null;
  metaCaloriasConsumidas?: number | null;
  metaCaloriasQueimadas?: number | null;
  pontosUser?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
