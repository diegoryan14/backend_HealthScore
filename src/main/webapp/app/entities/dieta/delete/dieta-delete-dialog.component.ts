import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDieta } from '../dieta.model';
import { DietaService } from '../service/dieta.service';

@Component({
  standalone: true,
  templateUrl: './dieta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DietaDeleteDialogComponent {
  dieta?: IDieta;

  protected dietaService = inject(DietaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dietaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
