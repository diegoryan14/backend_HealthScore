import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAtividadeFisica } from '../atividade-fisica.model';
import { AtividadeFisicaService } from '../service/atividade-fisica.service';

@Component({
  standalone: true,
  templateUrl: './atividade-fisica-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AtividadeFisicaDeleteDialogComponent {
  atividadeFisica?: IAtividadeFisica;

  protected atividadeFisicaService = inject(AtividadeFisicaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.atividadeFisicaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
