package com.ufape.estagios.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufape.estagios.model.Entrevista;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {

    boolean existsByCandidaturaCandidatoIdAndDataHora(Long candidatoId, LocalDateTime dataHora);
	
	boolean existsByCandidaturaVagaEmpresaIdAndDataHora(Long empresaId, LocalDateTime dataHora);
	
	Optional<Entrevista> findByCandidaturaId(Long candidaturaId);
    
}