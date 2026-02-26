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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
@MockitoSettings(strictness = Strictness.LENIENT)
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
        empresa = new Usuario(1L, "empresa@test.com", "password", UserRole.COMPANY);
        candidato = new Usuario(2L, "candidato@test.com", "password", UserRole.CANDIDATE);

        vagaRequestDTO = new VagaRequestDTO(
                "Desenvolvedor Java",
                "Vaga para desenvolvedor Java Júnior com conhecimento em Spring Boot",
                "Conhecimento em Java, Spring Boot, MySQL",
                "Tecnologia da Informação",
                Localizacao.HIBRIDO,
                "Integral - 8h às 18h",
                LocalDate.now().plusDays(30),
                "Vale alimentação, Vale transporte",
                "R$ 3.500,00",
                TipoVaga.EMPREGO
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
    @SuppressWarnings("unchecked")
    void deveListarVagasParaEstudantesComFiltros() {
        when(vagaRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Arrays.asList(vaga));

        List<VagaResponseDTO> response = vagaService.listarVagasParaEstudantes("Tecnologia", "EMPREGO", "HIBRIDO", "dataPublicacao");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        verify(vagaRepository).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    void deveCadastrarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        VagaResponseDTO response = vagaService.cadastrarVaga(vagaRequestDTO);

        assertNotNull(response);
        assertEquals("Desenvolvedor Java", response.titulo());
        verify(vagaRepository).save(any(Vaga.class));
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoTentaCadastrarVaga() {
        when(authentication.getPrincipal()).thenReturn(candidato);

        assertThrows(RuntimeException.class, () -> vagaService.cadastrarVaga(vagaRequestDTO));
    }

    @Test
    void deveListarVagasAtivasComSucesso() {
        when(vagaRepository.findByAtivaTrue()).thenReturn(Arrays.asList(vaga));

        List<VagaResponseDTO> response = vagaService.listarVagasAtivas();

        assertEquals(1, response.size());
        verify(vagaRepository).findByAtivaTrue();
    }

    @Test
    void deveListarMinhasVagasComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.findByEmpresa(empresa)).thenReturn(Arrays.asList(vaga));

        List<VagaResponseDTO> response = vagaService.listarMinhasVagas();

        assertEquals(1, response.size());
        verify(vagaRepository).findByEmpresa(empresa);
    }

    @Test
    void deveBuscarVagaPorIdComSucesso() {
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        VagaResponseDTO response = vagaService.buscarVagaPorId(1L);

        assertEquals(1L, response.id());
    }

    @Test
    void deveAtualizarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        VagaResponseDTO response = vagaService.atualizarVaga(1L, vagaRequestDTO);

        assertNotNull(response);
        verify(vagaRepository).save(any(Vaga.class));
    }

    @Test
    void deveDesativarVagaComSucesso() {
        when(authentication.getPrincipal()).thenReturn(empresa);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        vagaService.desativarVaga(1L);

        // CORREÇÃO: Usando isAtiva() em vez de getAtiva()
        assertFalse(vaga.isAtiva()); 
        verify(vagaRepository).save(vaga);
    }
}