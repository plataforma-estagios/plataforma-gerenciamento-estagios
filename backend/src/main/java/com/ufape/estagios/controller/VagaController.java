package com.ufape.estagios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.service.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @PostMapping
    public ResponseEntity<VagaResponseDTO> cadastrarVaga(@RequestBody @Valid VagaRequestDTO dto) {
        try {
            VagaResponseDTO response = vagaService.cadastrarVaga(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<VagaResponseDTO>> listarVagasEmAberto() {
        List<VagaResponseDTO> vagas = vagaService.listarVagasEmAberto();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/minhas-vagas")
    public ResponseEntity<List<VagaResponseDTO>> listarMinhasVagas() {
        try {
            List<VagaResponseDTO> vagas = vagaService.listarMinhasVagas();
            return ResponseEntity.ok(vagas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> buscarVagaPorId(@PathVariable Long id) {
        try {
            VagaResponseDTO vaga = vagaService.buscarVagaPorId(id);
            return ResponseEntity.ok(vaga);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> atualizarVaga(
            @PathVariable Long id,
            @RequestBody @Valid VagaRequestDTO dto) {
        try {
            VagaResponseDTO response = vagaService.atualizarVaga(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarVaga(@PathVariable Long id) {
        try {
            vagaService.desativarVaga(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}