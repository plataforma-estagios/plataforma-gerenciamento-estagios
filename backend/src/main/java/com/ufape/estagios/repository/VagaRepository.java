package com.ufape.estagios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Vaga;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long>, JpaSpecificationExecutor<Vaga> { 

    List<Vaga> findByEmpresaAndAtivaTrue(Empresa empresa);

    List<Vaga> findByAtivaTrue();

    List<Vaga> findByEmpresa(Empresa empresa);
}