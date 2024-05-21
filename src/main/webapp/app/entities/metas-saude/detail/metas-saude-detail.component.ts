import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMetasSaude } from '../metas-saude.model';

@Component({
  standalone: true,
  selector: 'app-metas-saude-detail',
  templateUrl: './metas-saude-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MetasSaudeDetailComponent {
  metasSaude = input<IMetasSaude | null>(null);

  previousState(): void {
    window.history.back();
  }
}
