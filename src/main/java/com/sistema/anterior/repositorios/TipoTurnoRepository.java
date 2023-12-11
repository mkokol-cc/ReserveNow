package com.sistema.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.modelo.usuario.Usuario;

@Repository
public interface TipoTurnoRepository extends JpaRepository<TipoTurno,Long>{
	List<TipoTurno> findByUsuario(Usuario usuario);
}
