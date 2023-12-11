package com.sistema.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.anterior.modelo.Reservante;
import com.sistema.modelo.usuario.Usuario;

public interface ReservanteRepository extends JpaRepository<Reservante,Long>{
	
	Reservante findByTelefono(String telefono);

	List<Reservante> findByUsuario(Usuario usuario);
	
	Reservante findByTelefonoAndUsuario(String telefono,Usuario usuario);
}

