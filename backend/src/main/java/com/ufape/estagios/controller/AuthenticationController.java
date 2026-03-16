package com.ufape.estagios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufape.estagios.dto.AuthenticationDTO;
import com.ufape.estagios.dto.CandidatoRegisterDTO;
import com.ufape.estagios.dto.EmpresaRegisterDTO;
import com.ufape.estagios.dto.LoginResponseDTO;
import com.ufape.estagios.dto.RegisterDTO;
import com.ufape.estagios.service.AuthorizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        String token = authorizationService.doLogin(data);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register/candidato")
    public ResponseEntity<?> registerCandidato(@RequestBody @Valid CandidatoRegisterDTO candidatoRegister) {
        authorizationService.candidatoRegistro(candidatoRegister);

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/register/empresa")
    public ResponseEntity<?> registerEmpresa(@RequestBody @Valid EmpresaRegisterDTO empresaRegister) {
        authorizationService.empresaRegistro(empresaRegister);

        return ResponseEntity.ok().build();
    }
}