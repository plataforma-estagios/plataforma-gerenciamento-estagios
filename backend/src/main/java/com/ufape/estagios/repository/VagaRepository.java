package com.ufape.estagios.repository;

import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {


	@Query("SELECT v FROM Vaga v WHERE v.empresa = :empresa and v.status = 'EM_ABERTO'")
    List<Vaga> findByEmpresaAndStatusIsEmAberto(@Param(value = "empresa") Usuario empresa);

    List<Vaga> findByStatus(StatusDaVaga statusDaVaga);

    List<Vaga> findByEmpresa(Usuario empresa);
}