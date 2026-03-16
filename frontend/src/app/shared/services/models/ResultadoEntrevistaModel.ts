import { StatusDaCandidatura } from './StatusDaCandidatura';

export interface ResultadoEntrevistaRequestModel {
  resultado: StatusDaCandidatura;
  comentario?: string;
}

export interface ResultadoEntrevistaResponseModel {
  candidaturaId: number;
  nomeCandidato: string;
  tituloVaga: string;
  statusAtual: StatusDaCandidatura;
  resultadoEntrevista: StatusDaCandidatura;
  comentarioEntrevista: string;
  dataResultadoEntrevista: string;
  mensagem: string;
}
