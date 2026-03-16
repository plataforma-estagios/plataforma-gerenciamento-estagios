package com.ufape.estagios.dto;

import com.ufape.estagios.model.UserRole;

public record RegisterDTO(String email, String senha, UserRole role, String nome, String curso, String instituicao ) {
}