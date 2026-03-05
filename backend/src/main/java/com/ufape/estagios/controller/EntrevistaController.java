package com.ufape.estagios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufape.estagios.dto.AgendamentoRequestDTO;
import com.ufape.estagios.dto.EntrevistaResponseDTO;
import com.ufape.estagios.service.EntrevistaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/entrevistas")
public class EntrevistaController {

    @Autowired
    private EntrevistaService entrevistaService;

    @PostMapping
    public ResponseEntity<EntrevistaResponseDTO> agendarEntrevista(@RequestBody @Valid AgendamentoRequestDTO dto) {
        
        EntrevistaResponseDTO response = entrevistaService.agendarEntrevista(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}