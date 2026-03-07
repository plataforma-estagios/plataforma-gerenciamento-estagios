package com.ufape.estagios.dto;

import java.time.LocalDate;

public record CandidatoRegisterDTO(String nome, String cpf, String curso, String instituicao, LocalDate dataNascimento, String email, String senha) {

}
