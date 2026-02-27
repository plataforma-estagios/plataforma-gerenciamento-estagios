package com.ufape.estagios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufape.estagios.dto.CandidaturaRequestDTO;
import com.ufape.estagios.model.Candidatura;
import com.ufape.estagios.service.CandidaturaService;

@RestController
@RequestMapping("/candidatura")
public class CandidaturaController {
	
	@Autowired
	private CandidaturaService candidaturaService;
	
	@PostMapping
	public ResponseEntity<?> cadastrarCandidatura(@RequestBody CandidaturaRequestDTO candidaturaRequest){
		candidaturaService.createCandidatura(candidaturaRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
