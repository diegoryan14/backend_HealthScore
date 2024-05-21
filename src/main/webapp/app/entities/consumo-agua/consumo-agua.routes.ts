import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsumoAguaComponent } from './list/consumo-agua.component';
import { ConsumoAguaDetailComponent } from './detail/consumo-agua-detail.component';
import { ConsumoAguaUpdateComponent } from './update/consumo-agua-update.component';
import ConsumoAguaResolve from './route/consumo-agua-routing-resolve.service';

const consumoAguaRoute: Routes = [
  {
    path: '',
    component: ConsumoAguaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumoAguaDetailComponent,
    resolve: {
      consumoAgua: ConsumoAguaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumoAguaUpdateComponent,
    resolve: {
      consumoAgua: ConsumoAguaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumoAguaUpdateComponent,
    resolve: {
      consumoAgua: ConsumoAguaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consumoAguaRoute;
