package com.sistema.examenes.anterior.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.CambioEstado;

@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado,Long>{
	 
}
