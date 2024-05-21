import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsultaEspecialistaComponent } from './list/consulta-especialista.component';
import { ConsultaEspecialistaDetailComponent } from './detail/consulta-especialista-detail.component';
import { ConsultaEspecialistaUpdateComponent } from './update/consulta-especialista-update.component';
import ConsultaEspecialistaResolve from './route/consulta-especialista-routing-resolve.service';

const consultaEspecialistaRoute: Routes = [
  {
    path: '',
    component: ConsultaEspecialistaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsultaEspecialistaDetailComponent,
    resolve: {
      consultaEspecialista: ConsultaEspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsultaEspecialistaUpdateComponent,
    resolve: {
      consultaEspecialista: ConsultaEspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsultaEspecialistaUpdateComponent,
    resolve: {
      consultaEspecialista: ConsultaEspecialistaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consultaEspecialistaRoute;
