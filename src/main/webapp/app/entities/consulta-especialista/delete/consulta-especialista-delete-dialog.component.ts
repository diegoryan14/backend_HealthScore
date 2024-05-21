import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsultaEspecialista } from '../consulta-especialista.model';
import { ConsultaEspecialistaService } from '../service/consulta-especialista.service';

@Component({
  standalone: true,
  templateUrl: './consulta-especialista-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsultaEspecialistaDeleteDialogComponent {
  consultaEspecialista?: IConsultaEspecialista;

  protected consultaEspecialistaService = inject(ConsultaEspecialistaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultaEspecialistaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
