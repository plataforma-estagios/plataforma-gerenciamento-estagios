package com.ufape.estagios.dto;

import java.time.LocalDateTime;

import com.ufape.estagios.model.TipoNotificacao;

public record NotificacaoResponseDTO(
        Long id,
        String mensagem,
        TipoNotificacao tipo,
        boolean lida,
        LocalDateTime dataEnvio
) {
}