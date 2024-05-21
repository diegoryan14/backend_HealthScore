import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConsumoAgua } from '../consumo-agua.model';

@Component({
  standalone: true,
  selector: 'app-consumo-agua-detail',
  templateUrl: './consumo-agua-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConsumoAguaDetailComponent {
  consumoAgua = input<IConsumoAgua | null>(null);

  previousState(): void {
    window.history.back();
  }
}
