import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AnuncioComponent } from './list/anuncio.component';
import { AnuncioDetailComponent } from './detail/anuncio-detail.component';
import { AnuncioUpdateComponent } from './update/anuncio-update.component';
import AnuncioResolve from './route/anuncio-routing-resolve.service';

const anuncioRoute: Routes = [
  {
    path: '',
    component: AnuncioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnuncioDetailComponent,
    resolve: {
      anuncio: AnuncioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnuncioUpdateComponent,
    resolve: {
      anuncio: AnuncioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnuncioUpdateComponent,
    resolve: {
      anuncio: AnuncioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default anuncioRoute;
