import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IPontuacaoUsuario {
  id: number;
  dataAlteracao?: dayjs.Dayjs | null;
  valorAlteracao?: number | null;
  motivo?: string | null;
  usuario?: IUsuario | null;
}

export type NewPontuacaoUsuario = Omit<IPontuacaoUsuario, 'id'> & { id: null };
