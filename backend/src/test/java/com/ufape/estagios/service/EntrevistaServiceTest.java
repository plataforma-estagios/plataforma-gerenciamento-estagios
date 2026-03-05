package com.ufape.estagios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ufape.estagios.dto.AgendamentoRequestDTO;
import com.ufape.estagios.dto.EntrevistaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Entrevista;
import com.ufape.estagios.model.FormatoEntrevista;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.EntrevistaRepository;

@ExtendWith(MockitoExtension.class)
public class EntrevistaServiceTest {

    @InjectMocks
    private EntrevistaService entrevistaService;

    @Mock
    private EntrevistaRepository entrevistaRepository;

    @Mock
    private CandidaturaRepository candidaturaRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Usuario empresa;
    private Usuario candidato;
    private Vaga vaga;
    private Candidatura candidatura;
    private AgendamentoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Preparando os dados falsos para o teste
        empresa = new Usuario();
        empresa.setId(1L);
        empresa.setRole(UserRole.COMPANY);

        candidato = new Usuario();
        candidato.setId(2L);
        candidato.setNome("João Estudante");
        candidato.setRole(UserRole.CANDIDATE);

        vaga = new Vaga();
        vaga.setId(1L);
        vaga.setTitulo("Vaga Desenvolvedor Java");
        vaga.setEmpresa(empresa);

        candidatura = new Candidatura();
        candidatura.setId(1L);
        candidatura.setUsuario(candidato);
        candidatura.setVaga(vaga);
        candidatura.setStatus(StatusDaCandidatura.EM_ANÁLISE);

        requestDTO = new AgendamentoRequestDTO(
                1L,
                LocalDateTime.now().plusDays(2),
                FormatoEntrevista.ONLINE,
                "Link do Google Meet"
        );
    }

    private void mockAutenticacao(Usuario usuarioLogado) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(usuarioLogado);
    }

    @Test
    void agendarEntrevista_ComSucesso() {
        mockAutenticacao(empresa);
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));
        when(entrevistaRepository.existsByCandidaturaUsuarioIdAndDataHora(anyLong(), any())).thenReturn(false);
        
        Entrevista entrevistaSalva = new Entrevista();
        entrevistaSalva.setId(1L);
        entrevistaSalva.setCandidatura(candidatura);
        entrevistaSalva.setDataHora(requestDTO.dataHora());
        entrevistaSalva.setFormato(requestDTO.formato());
        
        when(entrevistaRepository.save(any(Entrevista.class))).thenReturn(entrevistaSalva);

        // Ação
        EntrevistaResponseDTO response = entrevistaService.agendarEntrevista(requestDTO);

        // Verificações
        assertNotNull(response);
        assertEquals(StatusDaCandidatura.ENTREVISTA, candidatura.getStatus()); 
        assertEquals(1L, response.id());
        assertEquals("João Estudante", response.nomeCandidato());
        verify(entrevistaRepository, times(1)).save(any(Entrevista.class));
    }

    @Test
    void agendarEntrevista_NaoSendoEmpresa_DeveLancarException() {
        mockAutenticacao(candidato); 

        assertThrows(AccessDeniedException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
    }

    @Test
    void agendarEntrevista_VagaDeOutraEmpresa_DeveLancarException() {
        Usuario outraEmpresa = new Usuario();
        outraEmpresa.setId(99L);
        outraEmpresa.setRole(UserRole.COMPANY);
        mockAutenticacao(outraEmpresa); 

        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));

        assertThrows(AccessDeniedException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
    }

    @Test
    void agendarEntrevista_StatusInvalido_DeveLancarException() {
        mockAutenticacao(empresa);
        candidatura.setStatus(StatusDaCandidatura.APROVADA); 
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
        assertTrue(exception.getMessage().contains("finalizada ou cancelada"));
    }

    @Test
    void agendarEntrevista_ComConflitoDeHorario_DeveLancarException() {
        mockAutenticacao(empresa);
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));
        
        when(entrevistaRepository.existsByCandidaturaUsuarioIdAndDataHora(anyLong(), any())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
        assertTrue(exception.getMessage().contains("já possui uma entrevista agendada"));
    }
}