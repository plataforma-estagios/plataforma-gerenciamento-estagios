package com.ufape.estagios.service;

import java.util.Optional;

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
import com.ufape.estagios.model.Candidato;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Entrevista;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.EmpresaRepository;
import com.ufape.estagios.repository.EntrevistaRepository;

@Service
public class EntrevistaService {

    @Autowired
    private EntrevistaRepository entrevistaRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional
    public EntrevistaResponseDTO agendarEntrevista(AgendamentoRequestDTO dto) {
        Usuario empresa = getUsuarioAutenticado();

        if (empresa.getRole() != UserRole.COMPANY) {
            throw new AccessDeniedException("Apenas empresas podem agendar entrevistas.");
        }

        Candidatura candidatura = candidaturaRepository.findById(dto.candidaturaId())
                .orElseThrow(() -> new IdNotFoundException("Candidatura"));

        if (!candidatura.getVaga().getEmpresa().getId().equals(empresa.getId())) {
            throw new AccessDeniedException("Você não tem permissão para gerenciar esta candidatura.");
        }

        if (candidatura.getStatus() == StatusDaCandidatura.APROVADA ||
            candidatura.getStatus() == StatusDaCandidatura.REPROVADA ||
            candidatura.getStatus() == StatusDaCandidatura.CANCELADA) {
            throw new RuntimeException("Não é possível agendar entrevista para uma candidatura finalizada ou cancelada.");
        }

        boolean temConflito = entrevistaRepository.existsByCandidaturaCandidatoIdAndDataHora(
                candidatura.getCandidato().getId(), dto.dataHora());

        if (temConflito) {
            throw new ConflictException("O estudante já possui uma entrevista agendada para este horário exato.");
        }

        boolean conflitoEmpresa = entrevistaRepository.existsByCandidaturaVagaEmpresaIdAndDataHora(
                candidatura.getVaga().getEmpresa().getId(), dto.dataHora());

        if (conflitoEmpresa) {
            throw new ConflictException("Sua empresa já possui uma entrevista agendada para este horário.");
        }

        candidatura.setStatus(StatusDaCandidatura.ENTREVISTA);
        
        Entrevista entrevista = new Entrevista();
        entrevista.setCandidatura(candidatura);
        entrevista.setDataHora(dto.dataHora());
        entrevista.setFormato(dto.formato());
        entrevista.setDetalhes(dto.detalhes());

        entrevista = entrevistaRepository.save(entrevista);

        return new EntrevistaResponseDTO(
                entrevista.getId(),
                candidatura.getId(),
                candidatura.getCandidato().getNome(),
                candidatura.getVaga().getTitulo(),
                entrevista.getDataHora(),
                entrevista.getFormato(),
                entrevista.getDetalhes()
        );
    }

    public EntrevistaResponseDTO buscarEntrevistaPorCandidatura(Long candidaturaId) {
        Usuario usuario = getUsuarioAutenticado();
        Entrevista entrevista = entrevistaRepository.findByCandidaturaId(candidaturaId)
                .orElseThrow(() -> new IdNotFoundException("Entrevista"));
        
        Candidatura candidatura = entrevista.getCandidatura();
        
        if (usuario.getRole() == UserRole.COMPANY) {
        	Empresa empresaDonaDaVaga = candidatura.getVaga().getEmpresa();
        	Usuario usuarioDaEmpresa = empresaDonaDaVaga.getUsuario();
        	
            if (!usuarioDaEmpresa.equals(usuario.getId())) {
                throw new AccessDeniedException("Você não tem permissão para visualizar esta entrevista.");
            }
        } else if (usuario.getRole() == UserRole.CANDIDATE) {
        	Candidato candidatoDaEntrevista = entrevista.getCandidatura().getCandidato();
        	Usuario usuarioDoCandidato = candidatoDaEntrevista.getUsuario();
        	
            if (!usuarioDoCandidato.getId().equals(usuario.getId())) {
                throw new AccessDeniedException("Você não tem permissão para visualizar esta entrevista.");
            }
        } else {
            
        }
        
        return new EntrevistaResponseDTO(
                entrevista.getId(),
                candidatura.getId(),
                candidatura.getCandidato().getNome(),
                candidatura.getVaga().getTitulo(),
                entrevista.getDataHora(),
                entrevista.getFormato(),
                entrevista.getDetalhes()
        );
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
}