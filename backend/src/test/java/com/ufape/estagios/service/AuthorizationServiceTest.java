package com.ufape.estagios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ufape.estagios.dto.AuthenticationDTO;
import com.ufape.estagios.dto.CandidatoRegisterDTO;
import com.ufape.estagios.dto.EmpresaRegisterDTO;
import com.ufape.estagios.exception.ConflictException;
import com.ufape.estagios.exception.EmailAlreadyExistsException;
import com.ufape.estagios.model.Candidato;
import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.repository.CandidatoRepository;
import com.ufape.estagios.repository.EmpresaRepository;
import com.ufape.estagios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private TokenService tokenService;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setRole(UserRole.CANDIDATE);
    }

    @Test
    void loadUserByUsername_ComSucesso() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(usuario);

        var result = authorizationService.loadUserByUsername("teste@email.com");

        assertNotNull(result);
        assertEquals("teste@email.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_UsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername("teste@email.com");
        });
    }

    @Test
    void doLogin_ComSucesso() throws Exception {
        AuthenticationDTO dto = new AuthenticationDTO("teste@email.com", "123");

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(tokenService.generateToken(usuario)).thenReturn("token123");

        String token = authorizationService.doLogin(dto);

        assertEquals("token123", token);
        verify(tokenService).generateToken(usuario);
    }

    @Test
    void candidatoRegistro_ComSucesso() {
        CandidatoRegisterDTO dto = mock(CandidatoRegisterDTO.class);
        Candidato candidato = new Candidato();

        when(dto.email()).thenReturn("email@email.com");
        when(dto.senha()).thenReturn("123");
        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(null);
        when(candidatoRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(candidatoRepository.save(any())).thenReturn(candidato);

        authorizationService.candidatoRegistro(dto);

        verify(usuarioRepository, times(1)).save(any());
        verify(candidatoRepository, times(1)).save(any());
    }

    @Test
    void candidatoRegistro_CPFRepetido_DeveLancarException() {
        CandidatoRegisterDTO dto = mock(CandidatoRegisterDTO.class);

        Candidato candidatoExistente = new Candidato();
        candidatoExistente.setId(1L);

        when(candidatoRepository.findByCpf(any())).thenReturn(Optional.of(candidatoExistente));

        assertThrows(ConflictException.class, () -> {
            authorizationService.candidatoRegistro(dto);
        });
    }

    @Test
    void empresaRegistro_ComSucesso() {
        EmpresaRegisterDTO dto = mock(EmpresaRegisterDTO.class);
        Empresa empresa = new Empresa();

        when(dto.email()).thenReturn("empresa@email.com");
        when(dto.senha()).thenReturn("123");
        when(usuarioRepository.findByEmail("empresa@email.com")).thenReturn(null);
        when(empresaRepository.findByCnpj(any())).thenReturn(Optional.empty());
        when(empresaRepository.save(any())).thenReturn(empresa);

        authorizationService.empresaRegistro(dto);

        verify(usuarioRepository, times(1)).save(any());
        verify(empresaRepository, times(1)).save(any());
    }

    @Test
    void empresaRegistro_CNPJRepetido_DeveLancarException() {
        EmpresaRegisterDTO dto = mock(EmpresaRegisterDTO.class);

        Empresa empresaExistente = new Empresa();
        empresaExistente.setId(1L);

        when(empresaRepository.findByCnpj(any())).thenReturn(Optional.of(empresaExistente));

        assertThrows(ConflictException.class, () -> {
            authorizationService.empresaRegistro(dto);
        });
    }

    @Test
    void criarUsuario_EmailJaExiste_DeveLancarException() {

        CandidatoRegisterDTO dto = mock(CandidatoRegisterDTO.class);

        when(dto.email()).thenReturn("email@email.com");
        when(dto.senha()).thenReturn("123");

        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(usuario);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            authorizationService.candidatoRegistro(dto);
        });
    }
}