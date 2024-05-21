import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ControleMedicamentosComponent } from './list/controle-medicamentos.component';
import { ControleMedicamentosDetailComponent } from './detail/controle-medicamentos-detail.component';
import { ControleMedicamentosUpdateComponent } from './update/controle-medicamentos-update.component';
import ControleMedicamentosResolve from './route/controle-medicamentos-routing-resolve.service';

const controleMedicamentosRoute: Routes = [
  {
    path: '',
    component: ControleMedicamentosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ControleMedicamentosDetailComponent,
    resolve: {
      controleMedicamentos: ControleMedicamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ControleMedicamentosUpdateComponent,
    resolve: {
      controleMedicamentos: ControleMedicamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ControleMedicamentosUpdateComponent,
    resolve: {
      controleMedicamentos: ControleMedicamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default controleMedicamentosRoute;
