package com.ufape.estagios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufape.estagios.dto.NotificacaoResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.model.Notificacao;
import com.ufape.estagios.model.TipoNotificacao;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Transactional
    public void criarNotificacao(Usuario estudante, String mensagem, TipoNotificacao tipo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(estudante);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        
        notificacaoRepository.save(notificacao);
    }

    public List<NotificacaoResponseDTO> listarMinhasNotificacoes() {
        Usuario estudante = getUsuarioAutenticado();
        List<Notificacao> notificacoes = notificacaoRepository.findByUsuarioOrderByDataEnvioDesc(estudante);
        
        return notificacoes.stream()
                .map(n -> new NotificacaoResponseDTO(
                        n.getId(), 
                        n.getMensagem(), 
                        n.getTipo(), 
                        n.isLida(), 
                        n.getDataEnvio()))
                .toList();
    }

    @Transactional
    public void marcarComoLida(Long id) {
        Usuario estudante = getUsuarioAutenticado();
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Notificação"));

        if (!notificacao.getUsuario().getId().equals(estudante.getId())) {
            throw new AccessDeniedException("Não tens permissão para aceder a esta notificação.");
        }

        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    public long contarNotificacoesNaoLidas() {
        Usuario estudante = getUsuarioAutenticado();
        return notificacaoRepository.countByUsuarioAndLidaFalse(estudante);
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
}