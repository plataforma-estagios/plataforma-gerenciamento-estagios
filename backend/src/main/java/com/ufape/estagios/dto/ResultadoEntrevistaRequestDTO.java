package com.ufape.estagios.dto;

import com.ufape.estagios.model.StatusDaCandidatura;

import jakarta.validation.constraints.NotNull;

public record ResultadoEntrevistaRequestDTO(
        @NotNull(message = "O resultado é obrigatório")
        StatusDaCandidatura resultado,  

        String comentario               
) {}