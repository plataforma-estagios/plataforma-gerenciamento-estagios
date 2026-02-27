package com.ufape.estagios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long>{
	Optional<Candidatura> findByUsuarioAndVaga(Usuario usuario, Vaga vaga);
	
	List<Candidatura> findAllByVaga(Vaga vaga);
	
	List<Candidatura> findAllByUsuario(Usuario usuario);
}
