import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EspecialistaComponent } from './list/especialista.component';
import { EspecialistaDetailComponent } from './detail/especialista-detail.component';
import { EspecialistaUpdateComponent } from './update/especialista-update.component';
import EspecialistaResolve from './route/especialista-routing-resolve.service';

const especialistaRoute: Routes = [
  {
    path: '',
    component: EspecialistaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EspecialistaDetailComponent,
    resolve: {
      especialista: EspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EspecialistaUpdateComponent,
    resolve: {
      especialista: EspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EspecialistaUpdateComponent,
    resolve: {
      especialista: EspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default especialistaRoute;
