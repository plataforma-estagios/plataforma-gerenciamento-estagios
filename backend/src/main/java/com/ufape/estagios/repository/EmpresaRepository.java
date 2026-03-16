package com.ufape.estagios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Usuario;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

	Optional<Empresa> findByCnpj(String cnpj);

	Optional<Empresa> findByUsuario(Usuario usuario);

}
