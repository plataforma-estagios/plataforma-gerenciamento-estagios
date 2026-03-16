package com.ufape.estagios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufape.estagios.model.Notificacao;
import com.ufape.estagios.model.Usuario;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    
    List<Notificacao> findByUsuarioOrderByDataEnvioDesc(Usuario usuario);

    
    long countByUsuarioAndLidaFalse(Usuario usuario);
}