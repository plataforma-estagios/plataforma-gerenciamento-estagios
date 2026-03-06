package com.ufape.estagios.dto;

import com.ufape.estagios.model.UserRole;

public record EmpresaRegisterDTO(String razaoSocial, String setor, String cnpj, String link, String descricao, String localizacao,String email, String senha) {

}
