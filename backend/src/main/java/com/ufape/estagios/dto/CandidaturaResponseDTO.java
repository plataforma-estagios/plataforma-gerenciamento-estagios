package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.StatusDaCandidatura;

public record CandidaturaResponseDTO(String nomeDaVaga, String nomeDoCandidato, StatusDaCandidatura status, LocalDateTime data) {

}
