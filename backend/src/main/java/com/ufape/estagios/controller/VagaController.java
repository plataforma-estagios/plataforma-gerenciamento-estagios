package com.ufape.estagios.controller;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.service.VagaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaController {

	@Autowired
	private VagaService vagaService;

	@PostMapping
	public ResponseEntity<VagaResponseDTO> cadastrarVaga(@RequestBody @Valid VagaRequestDTO dto) {

		VagaResponseDTO response = vagaService.cadastrarVaga(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@GetMapping
	public ResponseEntity<List<VagaResponseDTO>> listarVagas(@RequestParam(required = false) String area,
			@RequestParam(required = false) String tipo, @RequestParam(required = false) String localizacao,
			@RequestParam(defaultValue = "dataPublicacao") String sortBy) {

		List<VagaResponseDTO> vagasResponse = vagaService.listarVagasParaEstudantes(area, tipo, localizacao, sortBy);
		return ResponseEntity.ok(vagasResponse);
	}

	@GetMapping("/minhas-vagas")
	public ResponseEntity<List<VagaResponseDTO>> listarMinhasVagas() {

		List<VagaResponseDTO> vagasResponse = vagaService.listarMinhasVagas();
		return ResponseEntity.ok(vagasResponse);

	}

	@GetMapping("/{id}")
	public ResponseEntity<VagaResponseDTO> buscarVagaPorId(@PathVariable Long id) {

		VagaResponseDTO vagaResponse = vagaService.buscarVagaPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(vagaResponse);

	}

	@PutMapping("/{id}")
	public ResponseEntity<VagaResponseDTO> atualizarVaga(@PathVariable Long id,
			@RequestBody @Valid VagaRequestDTO dto) {

		VagaResponseDTO response = vagaService.atualizarVaga(id, dto);
		return ResponseEntity.ok(response);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> desativarVaga(@PathVariable Long id) {
		vagaService.desativarVaga(id);
		return ResponseEntity.noContent().build();

	}
}