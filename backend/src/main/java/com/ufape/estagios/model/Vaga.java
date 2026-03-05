package com.ufape.estagios.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vagas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vaga {
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

    @Enumerated(EnumType.STRING)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDaVaga status;

    private boolean ativa = true;

    @PrePersist
    protected void onCreate() {
        if (dataPublicacao == null) {
            dataPublicacao = LocalDate.now();
        }
        if (status == null) {
            status = StatusDaVaga.EM_ABERTO;
        }
    }
}