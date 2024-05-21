import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQualidadeSono } from '../qualidade-sono.model';

@Component({
  standalone: true,
  selector: 'app-qualidade-sono-detail',
  templateUrl: './qualidade-sono-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QualidadeSonoDetailComponent {
  qualidadeSono = input<IQualidadeSono | null>(null);

  previousState(): void {
    window.history.back();
  }
}
