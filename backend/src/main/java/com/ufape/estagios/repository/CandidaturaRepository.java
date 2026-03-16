package com.ufape.estagios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufape.estagios.model.Candidato;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Vaga;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long>{
	
	List<Candidatura> findAllByVaga(Vaga vaga);
	
	List<Candidatura> findAllByCandidato(Candidato candidato);

	Optional<Candidatura> findByCandidatoAndVaga(Candidato candidato, Vaga vaga);
}
