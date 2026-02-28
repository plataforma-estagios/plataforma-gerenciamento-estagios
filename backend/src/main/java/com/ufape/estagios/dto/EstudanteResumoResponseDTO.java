package com.ufape.estagios.dto;

public record EstudanteResumoResponseDTO(
        Long id,
        String nome,
        String curso,
        String instituicao
) {}