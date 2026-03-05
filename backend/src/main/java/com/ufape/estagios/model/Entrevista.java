package com.ufape.estagios.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "entrevistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidatura_id", nullable = false)
    private Candidatura candidatura;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoEntrevista formato;

    @Column(length = 500)
    private String detalhes; 
}