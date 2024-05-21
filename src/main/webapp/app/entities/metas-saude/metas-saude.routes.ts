import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MetasSaudeComponent } from './list/metas-saude.component';
import { MetasSaudeDetailComponent } from './detail/metas-saude-detail.component';
import { MetasSaudeUpdateComponent } from './update/metas-saude-update.component';
import MetasSaudeResolve from './route/metas-saude-routing-resolve.service';

const metasSaudeRoute: Routes = [
  {
    path: '',
    component: MetasSaudeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MetasSaudeDetailComponent,
    resolve: {
      metasSaude: MetasSaudeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MetasSaudeUpdateComponent,
    resolve: {
      metasSaude: MetasSaudeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MetasSaudeUpdateComponent,
    resolve: {
      metasSaude: MetasSaudeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metasSaudeRoute;
