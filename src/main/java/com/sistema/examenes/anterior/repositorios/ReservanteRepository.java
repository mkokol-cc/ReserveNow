package com.sistema.examenes.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface ReservanteRepository extends JpaRepository<Reservante,Long>{
	
	Reservante findByTelefono(String telefono);

	List<Reservante> findByUsuario(Usuario usuario);
}

