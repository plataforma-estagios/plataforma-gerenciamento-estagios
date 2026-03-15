package com.ufape.estagios.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ufape.estagios.dto.CandidaturaResponseDTO;
import com.ufape.estagios.dto.ResultadoEntrevistaRequestDTO;
import com.ufape.estagios.model.Candidatura;

public class CandidaturaMapper {
	
	public static CandidaturaResponseDTO toDTO(Candidatura candidatura) {
		CandidaturaResponseDTO dto = new CandidaturaResponseDTO(candidatura.getId(), candidatura.getVaga().getId(), 
																candidatura.getVaga().getTitulo(), candidatura.getVaga().getEmpresa().getRazaoSocial(),
																candidatura.getCandidato().getNome(), 
																candidatura.getStatus(), candidatura.getDataDaCandidatura());
		
		return dto;
	}
	
	public static List<CandidaturaResponseDTO> toListDTO(List<Candidatura> candidaturas) {
        return candidaturas.stream()
                .map(CandidaturaMapper::toDTO)
                .collect(Collectors.toList());
    }
	
	public static Candidatura adicionarResultadoDaEntrevista(ResultadoEntrevistaRequestDTO dto, Candidatura candidatura ) {
		candidatura.setResultadoEntrevista(dto.resultado());
		candidatura.setComentarioEntrevista(dto.comentario());
		candidatura.setDataResultadoEntrevista(LocalDateTime.now());
		candidatura.setStatus(dto.resultado());
		
		return candidatura;
	}
}
