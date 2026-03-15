package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.StatusDaCandidatura;

public record ResultadoEntrevistaResponseDTO(
        Long candidaturaId,
        String nomeCandidato,
        String tituloVaga,
        StatusDaCandidatura statusAtual,
        StatusDaCandidatura resultadoEntrevista,
        String comentarioEntrevista,
        LocalDateTime dataResultadoEntrevista,
        String mensagem
) {}