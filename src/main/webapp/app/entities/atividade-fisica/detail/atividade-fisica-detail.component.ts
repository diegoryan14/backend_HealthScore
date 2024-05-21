import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAtividadeFisica } from '../atividade-fisica.model';

@Component({
  standalone: true,
  selector: 'app-atividade-fisica-detail',
  templateUrl: './atividade-fisica-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AtividadeFisicaDetailComponent {
  atividadeFisica = input<IAtividadeFisica | null>(null);

  previousState(): void {
    window.history.back();
  }
}
