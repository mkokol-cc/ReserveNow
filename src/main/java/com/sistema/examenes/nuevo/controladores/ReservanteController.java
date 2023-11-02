package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.servicios.UsuarioService;

@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class ReservanteController {

	@Autowired
	private ReservanteService reservanteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/clientes")
	public ResponseEntity<?> listarClientes() throws JsonProcessingException {
		try {
			List<Reservante> reservantes = reservanteService.listarReservanteDeUsuario(usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(reservantes);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@PutMapping("/clientes")
	public ResponseEntity<?> editarCliente(@Valid @RequestBody Reservante reservanteStr) throws JsonProcessingException {
		try {
			Reservante reservanteEditado = reservanteService.editarReservante(reservanteStr, usuarioService.getIdUsuarioActual());
			return ResponseEntity.ok(reservanteEditado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
