package com.ufape.estagios.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.ufape.estagios.dto.CandidaturaResponseDTO;
import com.ufape.estagios.model.Candidatura;

public class CandidaturaMapper {
	
	public static CandidaturaResponseDTO toDTO(Candidatura candidatura) {
		CandidaturaResponseDTO dto = new CandidaturaResponseDTO(candidatura.getId(), candidatura.getVaga().getId(), 
																candidatura.getVaga().getTitulo(), candidatura.getVaga().getEmpresa().getUsername(),
																candidatura.getUsuario().getUsername(), 
																candidatura.getStatus(), candidatura.getDataDaCandidatura());
		
		return dto;
	}
	
	public static List<CandidaturaResponseDTO> toListDTO(List<Candidatura> candidaturas) {
        return candidaturas.stream()
                .map(CandidaturaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
