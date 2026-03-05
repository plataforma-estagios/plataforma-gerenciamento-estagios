package com.ufape.estagios.dto;

import com.ufape.estagios.model.StatusDaCandidatura;

public record CandidaturaResumoResponseDTO(
        Long candidaturaId,
        StatusDaCandidatura status,
        EstudanteResumoResponseDTO estudante
) {}