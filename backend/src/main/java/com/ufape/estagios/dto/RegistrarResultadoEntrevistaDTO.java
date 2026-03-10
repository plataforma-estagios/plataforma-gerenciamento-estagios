package com.ufape.estagios.dto;

import com.ufape.estagios.model.StatusDaCandidatura;

import jakarta.validation.constraints.NotNull;

public record RegistrarResultadoEntrevistaDTO(

        @NotNull(message = "O resultado é obrigatório")
        StatusDaCandidatura resultado,  // APROVADA, REPROVADA ou PROXIMA_ETAPA

        String comentario               // opcional
) {
}