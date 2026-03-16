package com.ufape.estagios.model;


import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "razão social é obrigatório")
	private String razaoSocial;
	
	private String setor;
	
	@CNPJ(message = "cnpj não é válido")
	private String cnpj;
	
	private String link;
	
	private String descricao;
	
	private String localizacao;
	
	@OneToOne
	private Usuario usuario;
}
