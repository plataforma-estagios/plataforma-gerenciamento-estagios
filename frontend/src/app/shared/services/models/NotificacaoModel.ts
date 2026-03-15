export type TipoNotificacao = 'SUCESSO' | 'FALHA' | 'INFORMATIVO';

export interface NotificacaoModel {
  id: number;
  mensagem: string;
  tipo: TipoNotificacao;
  lida: boolean;
  dataEnvio: string;
}

