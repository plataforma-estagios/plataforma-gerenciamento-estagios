package com.ufape.estagios.dto;

import com.ufape.estagios.model.StatusDaCandidatura;

public record CandidaturaRequestDTO(Long vagaId, StatusDaCandidatura status) {

}
