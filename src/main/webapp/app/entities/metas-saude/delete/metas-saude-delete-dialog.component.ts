import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetasSaude } from '../metas-saude.model';
import { MetasSaudeService } from '../service/metas-saude.service';

@Component({
  standalone: true,
  templateUrl: './metas-saude-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetasSaudeDeleteDialogComponent {
  metasSaude?: IMetasSaude;

  protected metasSaudeService = inject(MetasSaudeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metasSaudeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
