package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.nuevo.servicios_interfaces.EstadoService;

@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@GetMapping("/estado")
	public ResponseEntity<?> getEstados() {
		try {
			List<Estado> estados = estadoService.listarEstados();
			return ResponseEntity.ok(estados);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
