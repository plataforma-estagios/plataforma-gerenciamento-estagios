package com.ufape.estagios.repository;

import com.ufape.estagios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Esse método mágico ensina o Spring a buscar um usuário pelo login
    UserDetails findByLogin(String login);
}