package com.ufape.estagios.mapper;

import com.ufape.estagios.dto.AgendamentoRequestDTO;
import com.ufape.estagios.dto.EntrevistaResponseDTO;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Entrevista;

public class EntrevistaMapper {
	
	public static Entrevista toEntity(AgendamentoRequestDTO dto) {
		Entrevista entrevista = new Entrevista();
		entrevista.setDataHora(dto.dataHora());
		entrevista.setFormato(dto.formato());
		entrevista.setDetalhes(dto.detalhes());
		
		return entrevista;
	}
	
	public static EntrevistaResponseDTO toDTO(Entrevista entrevista, Candidatura candidatura) {
		return new EntrevistaResponseDTO(
		entrevista.getId(),
        candidatura.getId(),
        candidatura.getCandidato().getNome(),
        candidatura.getVaga().getTitulo(),
        entrevista.getDataHora(),
        entrevista.getFormato(),
        entrevista.getDetalhes()
        );
	}
}
