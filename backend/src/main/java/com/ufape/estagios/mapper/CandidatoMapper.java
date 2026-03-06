package com.ufape.estagios.mapper;

import com.ufape.estagios.dto.CandidatoRegisterDTO;
import com.ufape.estagios.model.Candidato;

public class CandidatoMapper {
	public static Candidato toEntity(CandidatoRegisterDTO dto) {
		Candidato candidato = new Candidato();
		candidato.setNome(dto.nome());
		candidato.setCpf(dto.cpf());
		candidato.setDataDeNascimento(dto.dataNascimento());
		
		return candidato;
	}
}
