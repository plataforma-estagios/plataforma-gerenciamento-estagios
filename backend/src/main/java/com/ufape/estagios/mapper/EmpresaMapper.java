package com.ufape.estagios.mapper;

import com.ufape.estagios.dto.EmpresaRegisterDTO;
import com.ufape.estagios.model.Empresa;

public class EmpresaMapper {
	
	public static Empresa toEntity(EmpresaRegisterDTO dto) {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(dto.razaoSocial());
		empresa.setDescricao(dto.descricao());
		empresa.setCnpj(dto.cnpj());
		empresa.setLink(dto.link());
		empresa.setLocalizacao(dto.localizacao());
		empresa.setSetor(dto.setor());
		
		return empresa;
	}
}
