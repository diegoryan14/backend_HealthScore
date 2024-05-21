import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'healthScoreApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'usuario',
    data: { pageTitle: 'healthScoreApp.usuario.home.title' },
    loadChildren: () => import('./usuario/usuario.routes'),
  },
  {
    path: 'atividade-fisica',
    data: { pageTitle: 'healthScoreApp.atividadeFisica.home.title' },
    loadChildren: () => import('./atividade-fisica/atividade-fisica.routes'),
  },
  {
    path: 'dieta',
    data: { pageTitle: 'healthScoreApp.dieta.home.title' },
    loadChildren: () => import('./dieta/dieta.routes'),
  },
  {
    path: 'qualidade-sono',
    data: { pageTitle: 'healthScoreApp.qualidadeSono.home.title' },
    loadChildren: () => import('./qualidade-sono/qualidade-sono.routes'),
  },
  {
    path: 'controle-medicamentos',
    data: { pageTitle: 'healthScoreApp.controleMedicamentos.home.title' },
    loadChildren: () => import('./controle-medicamentos/controle-medicamentos.routes'),
  },
  {
    path: 'metas-saude',
    data: { pageTitle: 'healthScoreApp.metasSaude.home.title' },
    loadChildren: () => import('./metas-saude/metas-saude.routes'),
  },
  {
    path: 'anuncio',
    data: { pageTitle: 'healthScoreApp.anuncio.home.title' },
    loadChildren: () => import('./anuncio/anuncio.routes'),
  },
  {
    path: 'consulta-especialista',
    data: { pageTitle: 'healthScoreApp.consultaEspecialista.home.title' },
    loadChildren: () => import('./consulta-especialista/consulta-especialista.routes'),
  },
  {
    path: 'consumo-agua',
    data: { pageTitle: 'healthScoreApp.consumoAgua.home.title' },
    loadChildren: () => import('./consumo-agua/consumo-agua.routes'),
  },
  {
    path: 'especialista',
    data: { pageTitle: 'healthScoreApp.especialista.home.title' },
    loadChildren: () => import('./especialista/especialista.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
