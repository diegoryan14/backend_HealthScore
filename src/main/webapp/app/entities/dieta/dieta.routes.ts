import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DietaComponent } from './list/dieta.component';
import { DietaDetailComponent } from './detail/dieta-detail.component';
import { DietaUpdateComponent } from './update/dieta-update.component';
import DietaResolve from './route/dieta-routing-resolve.service';

const dietaRoute: Routes = [
  {
    path: '',
    component: DietaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DietaDetailComponent,
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DietaUpdateComponent,
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DietaUpdateComponent,
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dietaRoute;
