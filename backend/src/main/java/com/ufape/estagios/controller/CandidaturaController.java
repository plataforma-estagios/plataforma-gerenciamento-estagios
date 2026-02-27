package com.ufape.estagios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufape.estagios.dto.CandidaturaRequestDTO;
import com.ufape.estagios.dto.CandidaturaResponseDTO;
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
}
