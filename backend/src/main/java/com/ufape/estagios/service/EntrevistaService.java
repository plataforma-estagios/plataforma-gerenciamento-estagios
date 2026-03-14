package com.ufape.estagios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufape.estagios.dto.AgendamentoRequestDTO;
import com.ufape.estagios.dto.EntrevistaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.ConflictException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.mapper.EntrevistaMapper;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Entrevista;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.EntrevistaRepository;

@Service
public class EntrevistaService {

	@Autowired
	private EntrevistaRepository entrevistaRepository;

	@Autowired
	private CandidaturaRepository candidaturaRepository;

	@Transactional
	public EntrevistaResponseDTO agendarEntrevista(AgendamentoRequestDTO dto) {
		Candidatura candidatura = candidaturaRepository.findById(dto.candidaturaId())
				.orElseThrow(() -> new IdNotFoundException("Candidatura"));

		validarNovaEntrevista(candidatura, dto);

		candidatura.setStatus(StatusDaCandidatura.ENTREVISTA);

		Entrevista entrevista = EntrevistaMapper.toEntity(dto);
		entrevista.setCandidatura(candidatura);

		entrevista = entrevistaRepository.save(entrevista);

		return EntrevistaMapper.toDTO(entrevista, candidatura);
	}

	public EntrevistaResponseDTO buscarEntrevistaPorCandidatura(Long candidaturaId) {
		Entrevista entrevista = entrevistaRepository.findByCandidaturaId(candidaturaId)
				.orElseThrow(() -> new IdNotFoundException("Entrevista"));

		Candidatura candidatura = entrevista.getCandidatura();

		validarVizualizacaoDaEntrevista(candidatura);

		return EntrevistaMapper.toDTO(entrevista, candidatura);
	}

	private Usuario getUsuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (Usuario) authentication.getPrincipal();
	}

	private void validarNovaEntrevista(Candidatura candidatura, AgendamentoRequestDTO dto) {
		Usuario usuarioLogado = getUsuarioAutenticado();
		Empresa empresaDaVaga = candidatura.getVaga().getEmpresa();
				
		if (!empresaDaVaga.getUsuario().getId().equals(usuarioLogado.getId())) {
			throw new AccessDeniedException("Você não tem permissão para gerenciar esta candidatura.");
		}

		if (candidatura.getStatus() == StatusDaCandidatura.APROVADA
				|| candidatura.getStatus() == StatusDaCandidatura.REPROVADA
				|| candidatura.getStatus() == StatusDaCandidatura.CANCELADA) {
			throw new ConflictException(
					"Não é possível agendar entrevista para uma candidatura finalizada ou cancelada.");
		}

		boolean temConflito = entrevistaRepository
				.existsByCandidaturaCandidatoIdAndDataHora(candidatura.getCandidato().getId(), dto.dataHora());

		if (temConflito) {
			throw new ConflictException("O estudante já possui uma entrevista agendada para este horário exato.");
		}

		boolean conflitoEmpresa = entrevistaRepository.existsByCandidaturaVagaEmpresaIdAndDataHora(
				candidatura.getVaga().getEmpresa().getId(), dto.dataHora());

		if (conflitoEmpresa) {
			throw new ConflictException("Sua empresa já possui uma entrevista agendada para este horário.");
		}
	}

	private void validarVizualizacaoDaEntrevista(Candidatura candidatura) {
		Usuario usuario = getUsuarioAutenticado();

		if (usuario.getRole() == UserRole.COMPANY) {
			Usuario usuarioDaEmpresa = candidatura.getVaga().getEmpresa().getUsuario();

			if (!usuarioDaEmpresa.getId().equals(usuario.getId())) {
				throw new AccessDeniedException("Você não tem permissão para visualizar esta entrevista.");
			}
		} else if (usuario.getRole() == UserRole.CANDIDATE) {
			Usuario usuarioDoCandidato = candidatura.getCandidato().getUsuario();

			if (!usuarioDoCandidato.getId().equals(usuario.getId())) {
				throw new AccessDeniedException("Você não tem permissão para visualizar esta entrevista.");
			}
		}
	}
}