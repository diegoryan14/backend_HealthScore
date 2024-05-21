import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAnuncio } from '../anuncio.model';
import { AnuncioService } from '../service/anuncio.service';

@Component({
  standalone: true,
  templateUrl: './anuncio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AnuncioDeleteDialogComponent {
  anuncio?: IAnuncio;

  protected anuncioService = inject(AnuncioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.anuncioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
