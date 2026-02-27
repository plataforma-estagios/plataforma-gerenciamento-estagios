package com.ufape.estagios.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidatura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "O usuário é obrigatório")
    @ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario; //mudar para a classe estudante quando for implementada
	
	@NotNull(message = "A vaga é obrigatória")
    @ManyToOne
	@JoinColumn(name = "vaga_id")
	private Vaga vaga;
	
	@NotNull(message = "A data é obrigatória")
	private LocalDateTime dataDaCandidatura;
	
	@NotNull(message = "O status é obrigatório")
	@Enumerated(value = EnumType.STRING)
	private StatusDaCandidatura status;
}
