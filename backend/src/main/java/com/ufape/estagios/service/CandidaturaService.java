package com.ufape.estagios.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ufape.estagios.dto.CandidaturaRequestDTO;
import com.ufape.estagios.dto.CandidaturaResumoResponseDTO;
import com.ufape.estagios.dto.EstudanteResponseDTO;
import com.ufape.estagios.dto.EstudanteResumoResponseDTO;
import com.ufape.estagios.dto.ResultadoEntrevistaRequestDTO;
import com.ufape.estagios.dto.ResultadoEntrevistaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.ConflictException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.mapper.CandidaturaMapper;
import com.ufape.estagios.model.Candidato;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.CandidatoRepository;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.VagaRepository;

import jakarta.transaction.Transactional;

@Service	
public class CandidaturaService {
	
	@Autowired
	private CandidaturaRepository candidaturaRepository;
	
	@Autowired
	private VagaRepository vagaRepository;
	
	@Autowired
	private CandidatoRepository candidatoRepository;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Transactional
	public void createCandidatura(CandidaturaRequestDTO candidaturaRequest) {
		Vaga vaga = findVagaById(candidaturaRequest.vagaId());
		Candidato candidato = findCandidatoByUsuarioAutenticado();
		
		Candidatura candidatura = new Candidatura();
		
		candidatura.setDataDaCandidatura(LocalDateTime.now());
		candidatura.setStatus(StatusDaCandidatura.SUBMETIDA);
		candidatura.setVaga(vaga);
		candidatura.setCandidato(candidato);
		
		validarNovaCandidatura(candidatura);
		
		candidaturaRepository.save(candidatura);
		criarNovaNotificao(candidatura);
	}
	
	@Transactional
	public void atualizarCandidatura(Long id, CandidaturaRequestDTO candidaturaRequest) {
		Candidatura candidatura = candidaturaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Candidatura"));
		validarAcessoAVaga(candidatura.getVaga());

		if(candidatura.getStatus() == StatusDaCandidatura.APROVADA ||
				candidatura.getStatus() == StatusDaCandidatura.REPROVADA) {

			throw new ConflictException("Não é possível alterar uma candidatura finalizada");
		}
		
		candidatura.setStatus(candidaturaRequest.status());
		
		candidaturaRepository.save(candidatura);
		
		criarNovaNotificao(candidatura);
	}

	public List<Candidatura> listarCandidaturasDaVaga(Long vagaId){
		Vaga vaga = findVagaById(vagaId);
		
		validarAcessoAVaga(vaga);
		
		List<Candidatura> candidaturas = candidaturaRepository.findAllByVaga(vaga);
		
		return candidaturas;
	}
	
	public List<Candidatura> listarCandidaturasDoEstudante() {
		Candidato candidato = findCandidatoByUsuarioAutenticado();
		
		List<Candidatura> candidaturas = candidaturaRepository.findAllByCandidato(candidato);
		
		return candidaturas;
	}
	
	public List<CandidaturaResumoResponseDTO> listarResumoCandidaturasDaVaga(Long vagaId) {

		List<Candidatura> candidaturas = listarCandidaturasDaVaga(vagaId);

		return candidaturas.stream()
				.map(c -> new CandidaturaResumoResponseDTO(
						c.getId(),
						c.getStatus(),
						new EstudanteResumoResponseDTO(
								c.getCandidato().getId(),
								c.getCandidato().getNome(),
								c.getCandidato().getCurso(),
								c.getCandidato().getInstituicao()
						)
				))
				.toList();
	}

	public EstudanteResumoResponseDTO getPerfilResumidoDoEstudante(Long candidaturaId) {
		Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
				.orElseThrow(() -> new IdNotFoundException("Candidatura não encontrada"));
		validarAcessoAVaga(candidatura.getVaga());
		
		Candidato estudante = candidatura.getCandidato();
		return new EstudanteResumoResponseDTO(estudante.getId(), estudante.getNome(), estudante.getCurso(), estudante.getInstituicao());
	}

	public EstudanteResponseDTO getPerfilCompletoDoEstudante(Long candidaturaId) {
		Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
				.orElseThrow(() -> new IdNotFoundException("Candidatura não encontrada"));
		validarAcessoAVaga(candidatura.getVaga());
		
		Candidato estudante = candidatura.getCandidato();
		return new EstudanteResponseDTO(estudante.getId(), estudante.getNome(), estudante.getUsuario().getEmail(), estudante.getCurso(), estudante.getInstituicao());
	}
	
	private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
	
	private Candidato findCandidatoByUsuarioAutenticado() {
		Usuario usuario = getUsuarioAutenticado();
		Optional<Candidato> optionalCandidato = candidatoRepository.findByUsuario(usuario);
		
		return optionalCandidato.orElseThrow(() -> new IdNotFoundException("Usuario"));
	}
	
	private Vaga findVagaById(Long vagaId) {
		Vaga vaga = vagaRepository.findById(vagaId).orElseThrow(() -> new IdNotFoundException("Vaga"));
		
		return vaga;
	}	
	
	private void validarCandidaturaRepetida(Candidato candidato, Vaga vaga) {
		Optional<Candidatura> candidaturaRepetida = candidaturaRepository.findByCandidatoAndVaga(candidato, vaga);
		
		if(candidaturaRepetida.isPresent()) throw new ConflictException("Candidato já realizou a candidatura");
	}
	
	private void validarNovaCandidatura(Candidatura candidatura) {
		validarCandidaturaRepetida(candidatura.getCandidato(), candidatura.getVaga());
		
		Vaga vaga = candidatura.getVaga();
		if(vaga.getStatus() != StatusDaVaga.EM_ABERTO) {
			throw new ConflictException("Vaga não aceita mais candidaturas");
		}
	}
	
	private void validarAcessoAVaga(Vaga vaga) {
		Usuario usuario = getUsuarioAutenticado();
		Usuario usuarioDaEmpresaDonaDaVaga = vaga.getEmpresa().getUsuario();
		
		if(!usuario.getId().equals(usuarioDaEmpresaDonaDaVaga.getId())) {
			throw new AccessDeniedException("Você não tem permissão para gerenciar as candidaturas dessa vaga");
		}
		
	}
	
	private void criarNovaNotificao(Candidatura candidatura) {
		StatusDaCandidatura status = candidatura.getStatus();
		Vaga vaga = candidatura.getVaga();
		notificacaoService.criarNotificacao(candidatura.getCandidato().getUsuario(), status.getMensagemDeNotificacao(), status.getTipoNotificacao(), vaga.getTitulo());
        
	}

	@Transactional
	public ResultadoEntrevistaResponseDTO registrarResultadoEntrevista(
			Long candidaturaId,
			ResultadoEntrevistaRequestDTO dto) {

		Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
				.orElseThrow(() -> new IdNotFoundException("Candidatura"));

		validarAcessoAVaga(candidatura.getVaga());

		if (dto.resultado() != StatusDaCandidatura.APROVADA
				&& dto.resultado() != StatusDaCandidatura.REPROVADA
				&& dto.resultado() != StatusDaCandidatura.PROXIMA_ETAPA) {
			throw new ConflictException("Resultado inválido. Use: APROVADA, REPROVADA ou PROXIMA_ETAPA.");
		}

		candidatura = CandidaturaMapper.adicionarResultadoDaEntrevista(dto, candidatura);

		candidaturaRepository.save(candidatura);

		return new ResultadoEntrevistaResponseDTO(
				candidatura.getId(),
				candidatura.getCandidato().getNome(),
				candidatura.getVaga().getTitulo(),
				candidatura.getStatus(),
				candidatura.getResultadoEntrevista(),
				candidatura.getComentarioEntrevista(),
				candidatura.getDataResultadoEntrevista(),
				"Resultado da entrevista registrado com sucesso."
		);
	}
}
