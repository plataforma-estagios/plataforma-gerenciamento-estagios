package com.ufape.estagios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaga{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private String areaConhecimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Localizacao localizacao;
    
    private TipoVaga tipoVaga;

    private String periodoTurno;

    @Column(nullable = false)
    private LocalDate dataPublicacao;
    private LocalDate prazoCandidatura;
    private String beneficios;
    private String salario;
    private String requisitos;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Usuario empresa;
    
    private StatusDaVaga status;

    @PrePersist
    protected void onCreate(){
        if(dataPublicacao == null){
            dataPublicacao = LocalDate.now();
        }
    }
}
