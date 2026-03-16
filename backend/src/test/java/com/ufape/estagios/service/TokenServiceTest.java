package com.ufape.estagios.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.UserRole;

public class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();

        Field secretField = TokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, "segredo-super-seguro-para-testes-123456789123456789");
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setRole(UserRole.CANDIDATE);
        return usuario;
    }

    @Test
    void generateToken_ComSucesso() {
        Usuario usuario = criarUsuario();

        String token = tokenService.generateToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_DeveRetornarEmail() {
        Usuario usuario = criarUsuario();

        String token = tokenService.generateToken(usuario);

        String email = tokenService.validateToken(token);

        assertEquals("teste@email.com", email);
    }

    @Test
    void extractClaim_DeveExtrairSubject() {
        Usuario usuario = criarUsuario();

        String token = tokenService.generateToken(usuario);

        String subject = tokenService.extractClaim(token, claims -> claims.getSubject());

        assertEquals("teste@email.com", subject);
    }

    @Test
    void validateToken_TokenInvalido_DeveLancarException() {
        assertThrows(Exception.class, () -> {
            tokenService.validateToken("token.invalido.aqui");
        });
    }
}