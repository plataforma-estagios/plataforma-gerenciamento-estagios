package com.ufape.estagios.mapper;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;

public class VagaMapper {

    public static Vaga toEntity(VagaRequestDTO dto, Usuario empresa) {
        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.titulo());
        vaga.setDescricao(dto.descricao());
        vaga.setRequisitos(dto.requisitos());
        vaga.setAreaConhecimento(dto.areaConhecimento());
        vaga.setTipoVaga(dto.tipoVaga());
        vaga.setLocalizacao(dto.localizacao());
        vaga.setPeriodoTurno(dto.periodoTurno());
        vaga.setPrazoCandidatura(dto.prazoCandidatura());
        vaga.setBeneficios(dto.beneficios());
        vaga.setSalario(dto.salario());
        vaga.setEmpresa(empresa);

        return vaga;
    }

    public static Vaga updateVaga(Vaga vaga, VagaRequestDTO dto) {
        vaga.setTitulo(dto.titulo());
        vaga.setDescricao(dto.descricao());
        vaga.setRequisitos(dto.requisitos());
        vaga.setAreaConhecimento(dto.areaConhecimento());
        vaga.setTipoVaga(dto.tipoVaga());
        vaga.setLocalizacao(dto.localizacao());
        vaga.setPeriodoTurno(dto.periodoTurno());
        vaga.setPrazoCandidatura(dto.prazoCandidatura());
        vaga.setBeneficios(dto.beneficios());
        vaga.setSalario(dto.salario());

        return vaga;
    }
    
    public static VagaResponseDTO fromEntity(Vaga vaga) {
        return new VagaResponseDTO(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getEmpresa() != null ? vaga.getEmpresa().getEmail() : null,
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
                vaga.getEmpresa() != null ? vaga.getEmpresa().getId() : null,
                vaga.getEmpresa() != null ? vaga.getEmpresa().getEmail() : null,
                vaga.getStatus(),
                vaga.isAtiva());
    }
}
