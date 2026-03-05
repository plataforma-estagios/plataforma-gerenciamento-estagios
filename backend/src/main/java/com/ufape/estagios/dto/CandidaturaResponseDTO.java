package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.StatusDaCandidatura;

public record CandidaturaResponseDTO(Long id, Long vagaId, String nomeDaVaga, String nomeEmpresa, String nomeDoCandidato, StatusDaCandidatura status, LocalDateTime data) {

}
