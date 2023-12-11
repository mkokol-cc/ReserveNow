package com.sistema.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.anterior.modelo.Recurso;
import com.sistema.modelo.usuario.Usuario;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso,Long>{
	
	List<Recurso> findByUsuario(Usuario usuario);

}