import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService} from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

import { AppComponent } from './app.component';
import { PessoasModule } from './pessoas/pessoas.module';
import { LancamentosModule } from './lancamentos/lancamentos.module';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { LancamentoService } from './lancamentos/lancamento.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,

    ToastModule,
    ConfirmDialogModule,

    CoreModule,
    LancamentosModule,
    PessoasModule
  ],
  providers: [LancamentoService, MessageService, ConfirmationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
