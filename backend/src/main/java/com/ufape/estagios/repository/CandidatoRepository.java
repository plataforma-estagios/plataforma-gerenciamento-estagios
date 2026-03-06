package com.ufape.estagios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufape.estagios.model.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, Long>{

	Optional<Candidato> findByCpf(String cpf);

}
