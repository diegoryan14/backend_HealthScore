import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsumoAgua } from '../consumo-agua.model';
import { ConsumoAguaService } from '../service/consumo-agua.service';

@Component({
  standalone: true,
  templateUrl: './consumo-agua-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsumoAguaDeleteDialogComponent {
  consumoAgua?: IConsumoAgua;

  protected consumoAguaService = inject(ConsumoAguaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consumoAguaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
