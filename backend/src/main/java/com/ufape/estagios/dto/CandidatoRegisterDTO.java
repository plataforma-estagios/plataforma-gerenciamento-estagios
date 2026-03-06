package com.ufape.estagios.dto;

import java.time.LocalDate;

import com.ufape.estagios.model.UserRole;

public record CandidatoRegisterDTO(String nome, String cpf, LocalDate dataNascimento, String email, String senha) {

}
