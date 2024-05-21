import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEspecialista } from '../especialista.model';

@Component({
  standalone: true,
  selector: 'app-especialista-detail',
  templateUrl: './especialista-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EspecialistaDetailComponent {
  especialista = input<IEspecialista | null>(null);

  previousState(): void {
    window.history.back();
  }
}
