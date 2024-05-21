import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QualidadeSonoComponent } from './list/qualidade-sono.component';
import { QualidadeSonoDetailComponent } from './detail/qualidade-sono-detail.component';
import { QualidadeSonoUpdateComponent } from './update/qualidade-sono-update.component';
import QualidadeSonoResolve from './route/qualidade-sono-routing-resolve.service';

const qualidadeSonoRoute: Routes = [
  {
    path: '',
    component: QualidadeSonoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QualidadeSonoDetailComponent,
    resolve: {
      qualidadeSono: QualidadeSonoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QualidadeSonoUpdateComponent,
    resolve: {
      qualidadeSono: QualidadeSonoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QualidadeSonoUpdateComponent,
    resolve: {
      qualidadeSono: QualidadeSonoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default qualidadeSonoRoute;
