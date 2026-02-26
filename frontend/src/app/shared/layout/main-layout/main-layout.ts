import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../../component/header/header';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, Header],
  templateUrl: './main-layout.html',
})
export class MainLayout {

}
