package com.ufape.estagios.service;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.model.*;
import com.ufape.estagios.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VagaService vagaService;

    private Usuario empresa;
    private Usuario candidato;
    private VagaRequestDTO vagaRequestDTO;
    private Vaga vaga;

    @BeforeEach
    void setUp() {
        // CORREÇÃO 1: Usando o construtor cheio em vez de Setters
        empresa = new Usuario(1L, "empresa@test.com", "password", UserRole.COMPANY);
        candidato = new Usuario(2L, "candidato@test.com", "password", UserRole.CANDIDATE);

        // CORREÇÃO 2: Colocando Localizacao e TipoVaga na ordem correta do DTO
        vagaRequestDTO = new VagaRequestDTO(
                "Desenvolvedor Java",
                "Vaga para desenvolvedor Java Júnior com conhecimento em Spring Boot",
                "Conhecimento em Java, Spring Boot, MySQL",
                "Tecnologia da Informação",
                Localizacao.HIBRIDO, // Era o 5º argumento
                "Integral - 8h às 18h",
                LocalDate.now().plusDays(30),
                "Vale alimentação, Vale transporte",
                "R$ 3.500,00",
                TipoVaga.EMPREGO // Era o último argumento
        );

        vaga = new Vaga();
        vaga.setId(1L);
        vaga.setTitulo("Desenvolvedor Java");
        vaga.setDescricao("Vaga para desenvolvedor Java Júnior com conhecimento em Spring Boot");
        vaga.setRequisitos("Conhecimento em Java, Spring Boot, MySQL");
        vaga.setAreaConhecimento("Tecnologia da Informação");
        vaga.setTipoVaga(TipoVaga.EMPREGO);
        vaga.setLocalizacao(Localizacao.HIBRIDO);
        vaga.setPeriodoTurno("Integral - 8h às 18h");
        vaga.setDataPublicacao(LocalDate.now());
        vaga.setPrazoCandidatura(LocalDate.now().plusDays(30));
        vaga.setBeneficios("Vale alimentação, Vale transporte");
        vaga.setSalario("R$ 3.500,00");
        vaga.setEmpresa(empresa);
        vaga.setAtiva(true);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void deveCadastrarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        VagaResponseDTO response = vagaService.cadastrarVaga(vagaRequestDTO);

        assertNotNull(response);
        assertEquals("Desenvolvedor Java", response.titulo());
        assertEquals(empresa.getId(), response.empresaId());
        assertEquals(empresa.getEmail(), response.empresaEmail());
        assertTrue(response.ativa());
        verify(vagaRepository, times(1)).save(any(Vaga.class));
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoTentaCadastrarVaga() {
        when(authentication.getPrincipal()).thenReturn(candidato);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vagaService.cadastrarVaga(vagaRequestDTO);
        });

        assertEquals("Apenas empresas podem cadastrar vagas", exception.getMessage());
        verify(vagaRepository, never()).save(any(Vaga.class));
    }

    @Test
    void deveListarVagasAtivasComSucesso() {
        Vaga vaga2 = new Vaga();
        vaga2.setId(2L);
        vaga2.setTitulo("Desenvolvedor Python");
        vaga2.setDescricao("Vaga para Python");
        vaga2.setRequisitos("Python, Django");
        vaga2.setAreaConhecimento("TI");
        vaga2.setTipoVaga(TipoVaga.ESTAGIO);
        vaga2.setLocalizacao(Localizacao.REMOTO);
        vaga2.setEmpresa(empresa);
        vaga2.setAtiva(true);

        List<Vaga> vagas = Arrays.asList(vaga, vaga2);
        when(vagaRepository.findByAtivaTrue()).thenReturn(vagas);

        List<VagaResponseDTO> response = vagaService.listarVagasAtivas();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Desenvolvedor Java", response.get(0).titulo());
        assertEquals("Desenvolvedor Python", response.get(1).titulo());
        verify(vagaRepository, times(1)).findByAtivaTrue();
    }

    @Test
    void deveListarMinhasVagasComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        List<Vaga> vagas = Arrays.asList(vaga);
        when(vagaRepository.findByEmpresa(empresa)).thenReturn(vagas);

        List<VagaResponseDTO> response = vagaService.listarMinhasVagas();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Desenvolvedor Java", response.get(0).titulo());
        assertEquals(empresa.getId(), response.get(0).empresaId());
        verify(vagaRepository, times(1)).findByEmpresa(empresa);
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoTentaListarMinhasVagas() {
        when(authentication.getPrincipal()).thenReturn(candidato);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vagaService.listarMinhasVagas();
        });

        assertEquals("Apenas empresas podem listar suas vagas", exception.getMessage());
        verify(vagaRepository, never()).findByEmpresa(any());
    }

    @Test
    void deveBuscarVagaPorIdComSucesso() {
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        VagaResponseDTO response = vagaService.buscarVagaPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Desenvolvedor Java", response.titulo());
        assertEquals(TipoVaga.EMPREGO, response.tipoVaga());
        assertEquals(Localizacao.HIBRIDO, response.localizacao());
        verify(vagaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoVagaNaoEncontrada() {
        when(vagaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vagaService.buscarVagaPorId(999L);
        });

        assertEquals("Vaga não encontrada", exception.getMessage());
        verify(vagaRepository, times(1)).findById(999L);
    }

    @Test
    void deveAtualizarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        // CORREÇÃO 3: Ordem arrumada aqui também!
        VagaRequestDTO novosDados = new VagaRequestDTO(
                "Desenvolvedor Java Pleno",
                "Vaga atualizada",
                "Mais de 2 anos de experiência",
                "TI",
                Localizacao.REMOTO,
                "Flexível",
                LocalDate.now().plusDays(60),
                "Plano de saúde",
                "R$ 6.000,00",
                TipoVaga.EMPREGO
        );

        VagaResponseDTO response = vagaService.atualizarVaga(1L, novosDados);

        assertNotNull(response);
        verify(vagaRepository, times(1)).findById(1L);
        verify(vagaRepository, times(1)).save(any(Vaga.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmpresaTentaEditarVagaDeOutraEmpresa() {
        // CORREÇÃO 4: Usando construtor cheio
        Usuario outraEmpresa = new Usuario(3L, "outra@test.com", "password", UserRole.COMPANY);

        when(authentication.getPrincipal()).thenReturn(outraEmpresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vagaService.atualizarVaga(1L, vagaRequestDTO);
        });

        assertEquals("Você não tem permissão para editar esta vaga", exception.getMessage());
        verify(vagaRepository, never()).save(any(Vaga.class));
    }

    @Test
    void deveDesativarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        vagaService.desativarVaga(1L);

        verify(vagaRepository, times(1)).findById(1L);
        verify(vagaRepository, times(1)).save(vaga);
        assertFalse(vaga.getAtiva());
    }

    @Test
    void deveLancarExcecaoQuandoEmpresaTentaDesativarVagaDeOutraEmpresa() {
        // CORREÇÃO 5: Usando construtor cheio
        Usuario outraEmpresa = new Usuario(3L, "outra@test.com", "password", UserRole.COMPANY);

        when(authentication.getPrincipal()).thenReturn(outraEmpresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vagaService.desativarVaga(1L);
        });

        assertEquals("Você não tem permissão para desativar esta vaga", exception.getMessage());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverVagasAtivas() {
        when(vagaRepository.findByAtivaTrue()).thenReturn(Arrays.asList());

        List<VagaResponseDTO> response = vagaService.listarVagasAtivas();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(vagaRepository, times(1)).findByAtivaTrue();
    }
}