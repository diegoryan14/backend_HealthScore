import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IControleMedicamentos } from '../controle-medicamentos.model';
import { ControleMedicamentosService } from '../service/controle-medicamentos.service';

@Component({
  standalone: true,
  templateUrl: './controle-medicamentos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ControleMedicamentosDeleteDialogComponent {
  controleMedicamentos?: IControleMedicamentos;

  protected controleMedicamentosService = inject(ControleMedicamentosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.controleMedicamentosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
