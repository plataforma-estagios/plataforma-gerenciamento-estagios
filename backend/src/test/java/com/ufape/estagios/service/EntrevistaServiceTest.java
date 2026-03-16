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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ufape.estagios.dto.AgendamentoRequestDTO;
import com.ufape.estagios.dto.EntrevistaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.model.Candidato; // Import adicionado
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.model.Empresa; // Import adicionado
import com.ufape.estagios.model.Entrevista;
import com.ufape.estagios.model.FormatoEntrevista;
import com.ufape.estagios.model.StatusDaCandidatura;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.CandidaturaRepository;
import com.ufape.estagios.repository.EntrevistaRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    private Usuario usuarioEmpresa;
    private Empresa empresa;
    private Usuario usuarioCandidato;
    private Candidato candidato;
    private Vaga vaga;
    private Candidatura candidatura;
    private AgendamentoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // 1. Configurando Empresa
        usuarioEmpresa = new Usuario();
        usuarioEmpresa.setId(1L);
        usuarioEmpresa.setRole(UserRole.COMPANY);

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setUsuario(usuarioEmpresa);

        // 2. Configurando Candidato
        usuarioCandidato = new Usuario();
        usuarioCandidato.setId(2L);
        usuarioCandidato.setRole(UserRole.CANDIDATE);

        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNome("João Estudante");
        candidato.setUsuario(usuarioCandidato);

        // 3. Configurando Vaga e Candidatura (usando Empresa e Candidato)
        vaga = new Vaga();
        vaga.setId(1L);
        vaga.setTitulo("Vaga Desenvolvedor Java");
        vaga.setEmpresa(empresa);

        candidatura = new Candidatura();
        candidatura.setId(1L);
        candidatura.setCandidato(candidato); 
        candidatura.setVaga(vaga);
        candidatura.setStatus(StatusDaCandidatura.EM_ANALISE);

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
        mockAutenticacao(usuarioEmpresa);
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));
        
        // Atualizado para buscar pelo ID do Candidato
        when(entrevistaRepository.existsByCandidaturaCandidatoIdAndDataHora(anyLong(), any())).thenReturn(false);
        
        Entrevista entrevistaSalva = new Entrevista();
        entrevistaSalva.setId(1L);
        entrevistaSalva.setCandidatura(candidatura);
        entrevistaSalva.setDataHora(requestDTO.dataHora());
        entrevistaSalva.setFormato(requestDTO.formato());
        
        when(entrevistaRepository.save(any(Entrevista.class))).thenReturn(entrevistaSalva);

        EntrevistaResponseDTO response = entrevistaService.agendarEntrevista(requestDTO);

        assertNotNull(response);
        assertEquals(StatusDaCandidatura.ENTREVISTA, candidatura.getStatus()); 
        assertEquals(1L, response.id());
        assertEquals("João Estudante", response.nomeCandidato());
        verify(entrevistaRepository, times(1)).save(any(Entrevista.class));
    }

    @Test
    void agendarEntrevista_NaoSendoEmpresa_DeveLancarException() {
        mockAutenticacao(usuarioCandidato); 

        assertThrows(AccessDeniedException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
    }

    @Test
    void agendarEntrevista_VagaDeOutraEmpresa_DeveLancarException() {
        Usuario outraEmpresaUsuario = new Usuario();
        outraEmpresaUsuario.setId(99L);
        outraEmpresaUsuario.setRole(UserRole.COMPANY);
        mockAutenticacao(outraEmpresaUsuario); 

        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));

        assertThrows(AccessDeniedException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
    }

    @Test
    void agendarEntrevista_StatusInvalido_DeveLancarException() {
        mockAutenticacao(usuarioEmpresa);
        candidatura.setStatus(StatusDaCandidatura.APROVADA); 
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
        assertTrue(exception.getMessage().contains("finalizada ou cancelada"));
    }

    @Test
    void agendarEntrevista_ComConflitoDeHorario_DeveLancarException() {
        mockAutenticacao(usuarioEmpresa);
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));
            
        // Atualizado para buscar pelo ID do Candidato
        when(entrevistaRepository.existsByCandidaturaCandidatoIdAndDataHora(anyLong(), any())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            entrevistaService.agendarEntrevista(requestDTO);
        });
        assertTrue(exception.getMessage().contains("já possui uma entrevista agendada"));
    }
}