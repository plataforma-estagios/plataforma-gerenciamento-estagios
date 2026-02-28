package com.ufape.estagios.dto;

public record EstudanteResponseDTO(
        Long id,
        String nome,
        String email,
        String curso,
        String instituicao
) {}