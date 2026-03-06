package com.ufape.estagios.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufape.estagios.model.Entrevista;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {

        boolean existsByCandidaturaUsuarioIdAndDataHora(Long usuarioId, LocalDateTime dataHora);
	
	boolean existsByCandidaturaVagaEmpresaIdAndDataHora(Long empresaId, LocalDateTime dataHora);
	
	java.util.Optional<Entrevista> findByCandidaturaId(Long candidaturaId);
    
}