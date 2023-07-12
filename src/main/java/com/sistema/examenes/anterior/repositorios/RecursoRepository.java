package com.sistema.examenes.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso,Long>{
	
	List<Recurso> findByUsuario(Usuario usuario);

}