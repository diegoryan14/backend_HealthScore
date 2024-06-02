import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPontuacaoUsuario } from '../pontuacao-usuario.model';
import { PontuacaoUsuarioService } from '../service/pontuacao-usuario.service';

@Component({
  standalone: true,
  templateUrl: './pontuacao-usuario-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PontuacaoUsuarioDeleteDialogComponent {
  pontuacaoUsuario?: IPontuacaoUsuario;

  protected pontuacaoUsuarioService = inject(PontuacaoUsuarioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pontuacaoUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
