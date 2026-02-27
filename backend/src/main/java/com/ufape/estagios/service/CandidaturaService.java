package com.ufape.estagios.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ufape.estagios.dto.CandidaturaRequestDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.VagaRepository;

import jakarta.transaction.Transactional;

@Service	
public class CandidaturaService {
	
	@Autowired
	private CandidaturaRepository candidaturaRepository;
	
	@Autowired
	private VagaRepository vagaRepository;
	
	@Transactional
	public void saveCandidatura(Candidatura candidatura) {
		candidaturaRepository.save(candidatura);
	}
	
	@Transactional
	public void createCandidatura(CandidaturaRequestDTO candidaturaRequest) {
		Vaga vaga = findVagaById(candidaturaRequest.vagaId());
		Usuario usuario = getUsuarioAutenticado();
		
		Candidatura candidatura = new Candidatura();
		
		candidatura.setDataDaCandidatura(LocalDateTime.now());
		candidatura.setStatus(StatusDaCandidatura.SUBMETIDA);
		candidatura.setVaga(vaga);
		candidatura.setUsuario(usuario);
		
		validarNovaCandidatura(candidatura);
		
		saveCandidatura(candidatura);
	}
	
	@Transactional
	public void atualizarCandidatura(Long id, CandidaturaRequestDTO candidaturaRequest) {
		Candidatura candidatura = candidaturaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Candidatura"));
		validarAcessoACandidatura(candidatura);
		
		candidatura.setStatus(candidaturaRequest.status());
		
		saveCandidatura(candidatura);
	}

	public List<Candidatura> listarCandidaturasDaVaga(Long vagaId){
		Usuario usuario = getUsuarioAutenticado();
		Vaga vaga = findVagaById(vagaId);
		
		if(usuario.getId() != vaga.getEmpresa().getId()) {
			throw new AccessDeniedException("Você não tem permissão para listar as candidaturas dessa vaga");
		}
		
		List<Candidatura> candidaturas = candidaturaRepository.findAllByVaga(vaga);
		
		return candidaturas;
	}
	
	public List<Candidatura> listarCandidaturasDoEstudante() {
		Usuario usuario = getUsuarioAutenticado();
		
		if(usuario.getRole() != UserRole.CANDIDATE) throw new AccessDeniedException("Apenas estudantes podem listar suas candidaturas");
		
		List<Candidatura> candidaturas = candidaturaRepository.findAllByUsuario(usuario);
		
		return candidaturas;
	}
	
	private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
	
	private Vaga findVagaById(Long vagaId) {
		Vaga vaga = vagaRepository.findById(vagaId).orElseThrow(() -> new IdNotFoundException("Vaga"));
		
		return vaga;
	}	
	
	private void validarCandidaturaRepetida(Usuario usuario, Vaga vaga) {
		Optional<Candidatura> candidaturaRepetida = candidaturaRepository.findByUsuarioAndVaga(usuario, vaga);
		
		if(candidaturaRepetida.isPresent()) throw new RuntimeException("Estudante já realizou a candidatura");
	}
	
	private void validarNovaCandidatura(Candidatura candidatura) {
		validarCandidaturaRepetida(candidatura.getUsuario(), candidatura.getVaga());
		
		Vaga vaga = candidatura.getVaga();
		if(vaga.getStatus() != StatusDaVaga.EM_ABERTO) {
			throw new RuntimeException("Vaga não aceita mais candidaturas");
		}
	}
	
	private void validarAcessoACandidatura(Candidatura candidatura) {
		Usuario usuario = getUsuarioAutenticado();
		Usuario usuarioDonoDaVaga = candidatura.getVaga().getEmpresa();
		
		if(usuario.getId() != usuarioDonoDaVaga.getId()) {
			throw new AccessDeniedException("Você não tem permissão para gerenciar essa candidatura");
		}
		
	}
}
