import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAnuncio } from '../anuncio.model';

@Component({
  standalone: true,
  selector: 'app-anuncio-detail',
  templateUrl: './anuncio-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AnuncioDetailComponent {
  anuncio = input<IAnuncio | null>(null);

  previousState(): void {
    window.history.back();
  }
}
