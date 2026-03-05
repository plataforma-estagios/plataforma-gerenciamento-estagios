package com.ufape.estagios.dto;

import java.time.LocalDate;

import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.TipoVaga;

public record VagaResponseDTO(
        Long id,
        String titulo,
        String nomeEmpresa,
        String descricao,
        String requisitos,
        String areaConhecimento,
        TipoVaga tipoVaga,
        Localizacao localizacao,
        String periodoTurno,
        LocalDate dataPublicacao,
        LocalDate prazoCandidatura,
        String beneficios,
        String salario,
        Long empresaId,
        String empresaEmail,
        StatusDaVaga status,
        Boolean ativa) {
}