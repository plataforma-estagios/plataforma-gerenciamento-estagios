package com.ufape.estagios.repository;

import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; 
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long>, JpaSpecificationExecutor<Vaga> { 

    List<Vaga> findByEmpresaAndAtivaTrue(Usuario empresa);

    List<Vaga> findByAtivaTrue();

    List<Vaga> findByEmpresa(Usuario empresa);
}