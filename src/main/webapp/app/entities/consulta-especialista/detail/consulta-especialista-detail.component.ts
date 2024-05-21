import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConsultaEspecialista } from '../consulta-especialista.model';

@Component({
  standalone: true,
  selector: 'app-consulta-especialista-detail',
  templateUrl: './consulta-especialista-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConsultaEspecialistaDetailComponent {
  consultaEspecialista = input<IConsultaEspecialista | null>(null);

  previousState(): void {
    window.history.back();
  }
}
