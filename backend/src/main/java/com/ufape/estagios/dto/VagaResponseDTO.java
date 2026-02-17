package com.ufape.estagios.dto;

import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.TipoVaga;
import com.ufape.estagios.model.Vaga;

import java.time.LocalDate;

import java.time.LocalDate;

import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.TipoVaga;
import com.ufape.estagios.model.Vaga;

public record VagaResponseDTO(
        Long id,
        String titulo,
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
        StatusDaVaga status
) {
    public static VagaResponseDTO fromEntity(Vaga vaga) {
        return new VagaResponseDTO(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getDescricao(),
                vaga.getRequisitos(),
                vaga.getAreaConhecimento(),
                vaga.getTipoVaga(),
                vaga.getLocalizacao(),
                vaga.getPeriodoTurno(),
                vaga.getDataPublicacao(),
                vaga.getPrazoCandidatura(),
                vaga.getBeneficios(),
                vaga.getSalario(),
                vaga.getEmpresa().getId(),
                vaga.getEmpresa().getEmail(),
                vaga.getStatus()
        );
    }
}