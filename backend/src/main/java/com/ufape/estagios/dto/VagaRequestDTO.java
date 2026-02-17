package com.ufape.estagios.dto;

import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.TipoVaga;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record VagaRequestDTO(
        @NotBlank(message = "Título é orbigatório")
        @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
        String descricao,

        @NotBlank(message = "Requisitos são obrigatórios")
        @Size(max = 1000, message = "Requisitos devem ter no máxmo 1000 caracteres")
        String requisitos,

        @NotBlank(message = "Área do conhencimento é obrigatória")
        String areaConhecimento,

        @NotNull(message = "Localização é obrigatória")
        Localizacao localizacao,

        String periodoTurno,

        LocalDate prazoCandidatura,

        String beneficios,

        String salario,
        
        TipoVaga tipoVaga
){}