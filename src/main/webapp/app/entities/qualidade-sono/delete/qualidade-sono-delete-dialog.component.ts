import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQualidadeSono } from '../qualidade-sono.model';
import { QualidadeSonoService } from '../service/qualidade-sono.service';

@Component({
  standalone: true,
  templateUrl: './qualidade-sono-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QualidadeSonoDeleteDialogComponent {
  qualidadeSono?: IQualidadeSono;

  protected qualidadeSonoService = inject(QualidadeSonoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qualidadeSonoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
