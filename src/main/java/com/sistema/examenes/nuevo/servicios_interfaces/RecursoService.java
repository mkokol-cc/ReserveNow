package com.sistema.examenes.nuevo.servicios_interfaces;

import org.springframework.http.ResponseEntity;

import com.sistema.examenes.anterior.modelo.Recurso;

public interface RecursoService {
	
	public ResponseEntity<Recurso> guardarRecurso(Recurso recurso);

}
