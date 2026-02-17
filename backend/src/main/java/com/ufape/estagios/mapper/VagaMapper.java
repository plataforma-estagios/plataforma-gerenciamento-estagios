package com.ufape.estagios.mapper;

import com.ufape.estagios.dto.VagaRequestDTO;
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
}
