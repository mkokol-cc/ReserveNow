package com.sistema.examenes.anterior.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Long>{
	Estado findByNombre(String nombre);
}
