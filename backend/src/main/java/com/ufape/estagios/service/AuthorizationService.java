package com.ufape.estagios.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufape.estagios.dto.AuthenticationDTO;
import com.ufape.estagios.dto.CandidatoRegisterDTO;
import com.ufape.estagios.dto.EmpresaRegisterDTO;
import com.ufape.estagios.dto.RegisterDTO;
import com.ufape.estagios.exception.ConflictException;
import com.ufape.estagios.exception.EmailAlreadyExistsException;
import com.ufape.estagios.mapper.CandidatoMapper;
import com.ufape.estagios.mapper.EmpresaMapper;
import com.ufape.estagios.model.Candidato;
import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.repository.CandidatoRepository;
import com.ufape.estagios.repository.EmpresaRepository;
import com.ufape.estagios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private CandidatoRepository candidatoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;

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
	
	@Transactional
    public void candidatoRegistro(CandidatoRegisterDTO dto) {
    	
    	Candidato candidato = CandidatoMapper.toEntity(dto);
    	validarCPFRepetido(candidato);

    	Usuario usuario = criarUsuario(dto.email(), dto.senha(), UserRole.CANDIDATE);
    	
    	candidato.setUsuario(usuario);
    	
    	candidatoRepository.save(candidato);
    }
    
	@Transactional
    public void empresaRegistro(EmpresaRegisterDTO dto) {
    	
    	Empresa empresa = EmpresaMapper.toEntity(dto);
    	
    	validarCNPJRepetido(empresa);    	
    	
    	Usuario usuario = criarUsuario(dto.email(), dto.senha(), UserRole.COMPANY);
    	
    	empresa.setUsuario(usuario);
    	
    	empresaRepository.save(empresa);
    }
    
    private Usuario criarUsuario(String email, String senha, UserRole role) {

        if (usuarioRepository.findByEmail(email) != null)
            throw new EmailAlreadyExistsException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(senha);

        Usuario usuario = new Usuario(email, encryptedPassword, role);

        return usuarioRepository.save(usuario);
    }
    
    private void validarCPFRepetido(Candidato candidato) {
    	Optional<Candidato> optionalCandidato = candidatoRepository.findByCpf(candidato.getCpf());
    	
    	if(optionalCandidato.isPresent()) {
    		if(candidato.getId() == null) {
    			throw new ConflictException("Esse CPF já existe");
    		}
    		else {
    			Candidato candidatoRepetido = optionalCandidato.get();
    			
    			if(candidato.getId() != candidatoRepetido.getId()) {
    				throw new ConflictException("Esse CPF já existe");
    			}
    		}
    	}
    }
    
    private void validarCNPJRepetido(Empresa empresa) {
    	Optional<Empresa> optionalEmpresa = empresaRepository.findByCnpj(empresa.getCnpj());
    	
    	if(optionalEmpresa.isPresent()) {
    		if(empresa.getId() == null) {
    			throw new ConflictException("Esse CNPJ já existe");
    		}
    		else {
    			Empresa empresaRepetida = optionalEmpresa.get();
    			
    			if(empresa.getId() != empresaRepetida.getId()) {
    				throw new ConflictException("Esse CNPJ já existe");
    			}
    		}
    	}
    }
}
