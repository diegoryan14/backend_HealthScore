import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEspecialista } from 'app/entities/especialista/especialista.model';
import { TipoEspecialista } from 'app/entities/enumerations/tipo-especialista.model';
import { StatusConsulta } from 'app/entities/enumerations/status-consulta.model';

export interface IConsultaEspecialista {
  id: number;
  tipoEspecialista?: keyof typeof TipoEspecialista | null;
  dataHorarioConsulta?: dayjs.Dayjs | null;
  statusConsulta?: keyof typeof StatusConsulta | null;
  linkConsulta?: string | null;
  internalUser?: Pick<IUser, 'id'> | null;
  especialista?: IEspecialista | null;
}

export type NewConsultaEspecialista = Omit<IConsultaEspecialista, 'id'> & { id: null };
