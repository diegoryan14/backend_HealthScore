import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AtividadeFisicaComponent } from './list/atividade-fisica.component';
import { AtividadeFisicaDetailComponent } from './detail/atividade-fisica-detail.component';
import { AtividadeFisicaUpdateComponent } from './update/atividade-fisica-update.component';
import AtividadeFisicaResolve from './route/atividade-fisica-routing-resolve.service';

const atividadeFisicaRoute: Routes = [
  {
    path: '',
    component: AtividadeFisicaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AtividadeFisicaDetailComponent,
    resolve: {
      atividadeFisica: AtividadeFisicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AtividadeFisicaUpdateComponent,
    resolve: {
      atividadeFisica: AtividadeFisicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AtividadeFisicaUpdateComponent,
    resolve: {
      atividadeFisica: AtividadeFisicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default atividadeFisicaRoute;
