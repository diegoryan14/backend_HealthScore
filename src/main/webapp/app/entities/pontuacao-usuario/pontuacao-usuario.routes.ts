import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PontuacaoUsuarioComponent } from './list/pontuacao-usuario.component';
import { PontuacaoUsuarioDetailComponent } from './detail/pontuacao-usuario-detail.component';
import { PontuacaoUsuarioUpdateComponent } from './update/pontuacao-usuario-update.component';
import PontuacaoUsuarioResolve from './route/pontuacao-usuario-routing-resolve.service';

const pontuacaoUsuarioRoute: Routes = [
  {
    path: '',
    component: PontuacaoUsuarioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PontuacaoUsuarioDetailComponent,
    resolve: {
      pontuacaoUsuario: PontuacaoUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PontuacaoUsuarioUpdateComponent,
    resolve: {
      pontuacaoUsuario: PontuacaoUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PontuacaoUsuarioUpdateComponent,
    resolve: {
      pontuacaoUsuario: PontuacaoUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pontuacaoUsuarioRoute;
