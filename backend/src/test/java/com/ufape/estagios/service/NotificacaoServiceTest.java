package com.ufape.estagios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

import com.ufape.estagios.dto.NotificacaoResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.model.Notificacao;
import com.ufape.estagios.model.TipoNotificacao;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.NotificacaoRepository;

@ExtendWith(MockitoExtension.class)
public class NotificacaoServiceTest {

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Usuario estudante;
    private Notificacao notificacao;

    @BeforeEach
    void setUp() {
        estudante = new Usuario();
        estudante.setId(1L);
        estudante.setNome("João Estudante");

        notificacao = new Notificacao();
        notificacao.setId(10L);
        notificacao.setUsuario(estudante);
        notificacao.setMensagem("Mensagem de teste");
        notificacao.setTipo(TipoNotificacao.SUCESSO);
        notificacao.setLida(false);
        notificacao.setDataEnvio(LocalDateTime.now());
    }

    private void mockAutenticacao(Usuario usuarioLogado) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(usuarioLogado);
    }

    @Test
    void criarNotificacao_DeveSalvarNoRepositorio() {
        notificacaoService.criarNotificacao(estudante, "Aprovado", TipoNotificacao.SUCESSO);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    void listarMinhasNotificacoes_DeveRetornarListaDeDTOs() {
        mockAutenticacao(estudante);
        when(notificacaoRepository.findByUsuarioOrderByDataEnvioDesc(estudante))
                .thenReturn(Arrays.asList(notificacao));

        List<NotificacaoResponseDTO> resultado = notificacaoService.listarMinhasNotificacoes();

        assertEquals(1, resultado.size());
        assertEquals("Mensagem de teste", resultado.get(0).mensagem());
    }

    @Test
    void marcarComoLida_SendoDonoDaNotificacao_DeveMudarStatus() {
        mockAutenticacao(estudante);
        when(notificacaoRepository.findById(10L)).thenReturn(Optional.of(notificacao));

        notificacaoService.marcarComoLida(10L);

        assertTrue(notificacao.isLida());
        verify(notificacaoRepository, times(1)).save(notificacao);
    }

    @Test
    void marcarComoLida_NaoSendoDono_DeveLancarExcecao() {
        Usuario outroEstudante = new Usuario();
        outroEstudante.setId(99L);
        mockAutenticacao(outroEstudante);
        
        when(notificacaoRepository.findById(10L)).thenReturn(Optional.of(notificacao));

        assertThrows(AccessDeniedException.class, () -> {
            notificacaoService.marcarComoLida(10L);
        });
    }

    @Test
    void contarNotificacoesNaoLidas_DeveRetornarQuantidadeCorreta() {
        mockAutenticacao(estudante);
        when(notificacaoRepository.countByUsuarioAndLidaFalse(estudante)).thenReturn(3L);

        long total = notificacaoService.contarNotificacoesNaoLidas();

        assertEquals(3L, total);
    }
}