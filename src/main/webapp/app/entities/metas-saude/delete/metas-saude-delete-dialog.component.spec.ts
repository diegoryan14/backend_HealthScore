jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MetasSaudeService } from '../service/metas-saude.service';

import { MetasSaudeDeleteDialogComponent } from './metas-saude-delete-dialog.component';

describe('MetasSaude Management Delete Component', () => {
  let comp: MetasSaudeDeleteDialogComponent;
  let fixture: ComponentFixture<MetasSaudeDeleteDialogComponent>;
  let service: MetasSaudeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MetasSaudeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MetasSaudeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MetasSaudeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MetasSaudeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
