package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.FormatoEntrevista;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AgendamentoRequestDTO(
        
        @NotNull(message = "O ID da candidatura é obrigatório")
        Long candidaturaId,

        @NotNull(message = "A data e hora são obrigatórias")
        @Future(message = "A data da entrevista deve ser no futuro")
        LocalDateTime dataHora,

        @NotNull(message = "O formato da entrevista é obrigatório")
        FormatoEntrevista formato,

        String detalhes
) {
}