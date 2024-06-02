import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPontuacaoUsuario } from '../pontuacao-usuario.model';

@Component({
  standalone: true,
  selector: 'app-pontuacao-usuario-detail',
  templateUrl: './pontuacao-usuario-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PontuacaoUsuarioDetailComponent {
  pontuacaoUsuario = input<IPontuacaoUsuario | null>(null);

  previousState(): void {
    window.history.back();
  }
}
