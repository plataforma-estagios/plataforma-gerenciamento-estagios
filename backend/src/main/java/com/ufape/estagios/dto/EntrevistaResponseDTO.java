package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.FormatoEntrevista;

public record EntrevistaResponseDTO(
        Long id,
        Long candidaturaId,
        String nomeCandidato,
        String tituloVaga,
        LocalDateTime dataHora,
        FormatoEntrevista formato,
        String detalhes
) {
}