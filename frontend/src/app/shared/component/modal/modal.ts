import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class Modal {
  @Input() open = false;
  @Output() closeModal = new EventEmitter<void>();

  @Input() title = '';

  onBackdropClick() {
    this.closeModal.emit();
  }
}
