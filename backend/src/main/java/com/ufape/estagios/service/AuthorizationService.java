package com.ufape.estagios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufape.estagios.dto.AuthenticationDTO;
import com.ufape.estagios.dto.RegisterDTO;
import com.ufape.estagios.exception.EmailAlreadyExistsException;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.UsuarioRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    
    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = usuarioRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found: " + email);
        return user;
    }
    
	public String doLogin(AuthenticationDTO loginRequest) {

		var manager = authenticationConfiguration.getAuthenticationManager();
		var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

		var auth = manager.authenticate(usernamePassword);

		var token = tokenService.generateToken((Usuario) auth.getPrincipal());

		return token;

	}
    
    public void doRegister(RegisterDTO registerRequest) {
    	if (this.usuarioRepository.findByEmail(registerRequest.email()) != null)
            throw new EmailAlreadyExistsException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());
        Usuario newUser = new Usuario(registerRequest.email(), encryptedPassword, registerRequest.role());

        newUser.setNome(registerRequest.nome());
        newUser.setCurso(registerRequest.curso());
        newUser.setInstituicao(registerRequest.instituicao());

        this.usuarioRepository.save(newUser);
    }

}
