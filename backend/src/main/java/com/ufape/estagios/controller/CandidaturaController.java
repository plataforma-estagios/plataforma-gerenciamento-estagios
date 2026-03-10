package com.ufape.estagios.controller;

import java.util.List;

import com.ufape.estagios.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ufape.estagios.dto.RegistrarResultadoEntrevistaResponseDTO;
import com.ufape.estagios.dto.RegistrarResultadoEntrevistaDTO;
import com.ufape.estagios.mapper.CandidaturaMapper;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.service.CandidaturaService;

@RestController
@RequestMapping("/api/candidatura")
public class CandidaturaController {
	
	@Autowired
	private CandidaturaService candidaturaService;
	
	@PostMapping
	public ResponseEntity<?> cadastrarCandidatura(@RequestBody CandidaturaRequestDTO candidaturaRequest){
		candidaturaService.createCandidatura(candidaturaRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarCandidatura(@PathVariable Long id, @RequestBody CandidaturaRequestDTO candidaturaRequest){
		candidaturaService.atualizarCandidatura(id, candidaturaRequest);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/{id}/resultado-entrevista")
	public ResponseEntity<RegistrarResultadoEntrevistaResponseDTO> registrarResultadoEntrevista(
			@PathVariable Long id,
			@RequestBody @Valid RegistrarResultadoEntrevistaDTO dto) {
		RegistrarResultadoEntrevistaResponseDTO response =
				candidaturaService.registrarResultadoEntrevista(id, dto);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/vaga/{vagaId}")
	public ResponseEntity<?> listarCandidaturasDaVaga(@PathVariable Long vagaId){
		List<Candidatura> candidaturas = candidaturaService.listarCandidaturasDaVaga(vagaId);
		
		List<CandidaturaResponseDTO> dto = CandidaturaMapper.toListDTO(candidaturas);
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
	
	@GetMapping("/minhas-candidaturas")
	public ResponseEntity<?> listarCandidaturasDoEstudante(){
		List<Candidatura> candidaturas = candidaturaService.listarCandidaturasDoEstudante();
		
		List<CandidaturaResponseDTO> dto = CandidaturaMapper.toListDTO(candidaturas);
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/{candidaturaId}/perfil/resumo")
	public ResponseEntity<EstudanteResumoResponseDTO> getPerfilResumido(@PathVariable Long candidaturaId) {
		return ResponseEntity.ok(candidaturaService.getPerfilResumidoDoEstudante(candidaturaId));
	}

	@GetMapping("/{candidaturaId}/perfil")
	public ResponseEntity<EstudanteResponseDTO> getPerfilCompleto(@PathVariable Long candidaturaId) {
		return ResponseEntity.ok(candidaturaService.getPerfilCompletoDoEstudante(candidaturaId));
	}
}


